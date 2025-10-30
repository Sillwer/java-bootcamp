package edu.school21.orm;

import edu.school21.annotation.OrmColumn;
import edu.school21.annotation.OrmColumnId;
import edu.school21.annotation.OrmEntity;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class OrmManager {
    private final EmbeddedDatabase dataSource;
    private final HashSet<Class<?>> SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
            String.class, Integer.class, Double.class, Boolean.class, Long.class
    ));

    OrmManager(EmbeddedDatabase dataSource) {
        this.dataSource = dataSource;
    }

    public void init(Class<?> objClass) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            if (connection.getMetaData().getDriverName().equals("postgresql")) {
                throw new SQLException("Unsupported SQL driver: [" + connection.getMetaData().getDriverName() + "]. Use postgresql.");
            }
            OrmEntity ormEntity = objClass.getAnnotation(OrmEntity.class);
            if (ormEntity == null) {
                throw new SQLException("Use @OrmEntity");
            }

            StringBuilder sqlQueryBuilder = new StringBuilder();

            String tableName = ormEntity.table();
            sqlQueryBuilder.append(String.format("drop table if exists %s; create table %s (", tableName, tableName));

            ArrayList<String> columns = new ArrayList<>();

            List<Field> idFields = Arrays.stream(objClass.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(OrmColumnId.class))
                    .collect(Collectors.toList());
            if (idFields.size() > 1) {
                throw new SQLException("@OrmColumnId defined [" + idFields.size() + "] times. Exactly one expected.");
            } else if (idFields.size() == 1) {
                columns.add(idFields.get(0).getName() + " BIGSERIAL PRIMARY KEY");
            }

            List<Field> columnFields = Arrays.stream(objClass.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(OrmColumn.class))
                    .collect(Collectors.toList());

            for (Field f : columnFields) {
                OrmColumn ormColumn = f.getAnnotation(OrmColumn.class);

                Class<?> type = f.getType();
                if (!SUPPORTED_TYPES.contains(type)) {
                    throw new SQLException("@OrmColumn unsupported type [" + type + "]. " +
                            "Use: " + Arrays.stream(SUPPORTED_TYPES.toArray())
                            .map(Object::toString)
                            .collect(Collectors.joining(", ")));
                }

                if (type.equals(String.class) && ormColumn.length() < 5) {
                    throw new SQLException("@OrmColumnId unexpected 'length' = [" + ormColumn.length()
                            + "] named " + ormColumn.name() + ". Expected length >= 5");
                }

                String ormType = postgresType(type, ormColumn);
                columns.add(ormColumn.name() + " " + ormType);
            }
            sqlQueryBuilder.append(String.join(", ", columns));
            sqlQueryBuilder.append(");");

            String sqlQuery = sqlQueryBuilder.toString();
            System.out.println("SQL query: " + sqlQuery);

            statement.executeUpdate(sqlQuery);
        }
    }

    public void save(Object entity) throws Exception {
        OrmEntity ormEntity = entity.getClass().getAnnotation(OrmEntity.class);
        if (ormEntity == null) {
            throw new Exception("Use @OrmEntity to set table name");
        }

        List<Field> idFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(OrmColumnId.class))
                .collect(Collectors.toList());
        if (idFields.size() != 1) {
            throw new SQLException("@OrmColumnId defined [" + idFields.size() + "] times. Exactly one expected.");
        }
        Field idField = idFields.get(0);

        List<Field> columnFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(OrmColumn.class))
                .collect(Collectors.toList());
        if (columnFields.isEmpty()) {
            throw new Exception("Use @OrmColumn to set table entities");
        }

        StringBuilder sqlQueryBuilder = new StringBuilder("insert into " + ormEntity.table() + " ");
        sqlQueryBuilder
                .append("(")
                .append(columnFields.stream().map(f -> f.getAnnotation(OrmColumn.class).name()).collect(Collectors.joining(", ")))
                .append(") values (")
                .append(columnFields.stream().map(f -> {
                            try {
                                f.setAccessible(true);
                                if (f.getType() == String.class) {
                                    return "'" + f.get(entity) + "'";
                                } else {
                                    return f.get(entity).toString();
                                }
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }).collect(Collectors.joining(", "))
                )
                .append(");");

        String query = sqlQueryBuilder.toString();
        System.out.println("SQL query: " + sqlQueryBuilder.toString());

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                idField.setAccessible(true);
                idField.set(entity, rs.getLong(1));
            }
        }
    }

    public <T> T findById(Long id, Class<T> aClass) throws Exception {
        if (id == null || aClass == null) {
            throw new Exception("findById(" + id + ", " + aClass + ")");
        }

        String tableName = aClass.getAnnotation(OrmEntity.class).table();
        Field idField = Arrays.stream(aClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(OrmColumnId.class))
                .findFirst().get();
        String sqlQuery = String.format("select * from %s where %s = %d;", tableName, idField.getName(), id);
        System.out.println("SQL query: " + sqlQuery);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sqlQuery);
            if (rs.next()) {
                List<Field> columnFields = Arrays.stream(aClass.getDeclaredFields())
                        .filter(f -> f.isAnnotationPresent(OrmColumn.class)).collect(Collectors.toList());

                List<Class<?>> paramTypes = new ArrayList<>();
                List<Object> paramValues = new ArrayList<>();
                paramTypes.add(idField.getType());
                paramValues.add(rs.getObject(idField.getName(), idField.getType()));

                for (Field field : columnFields) {
                    String columnName = field.getAnnotation(OrmColumn.class).name();
                    paramTypes.add(field.getType());
                    paramValues.add(rs.getObject(columnName, field.getType()));
                }

                Constructor<T> constructor = aClass.getDeclaredConstructor(paramTypes.toArray(new Class<?>[0]));
                return constructor.newInstance(paramValues.toArray());
            }
        }

        return null;
    }

    public void update(Object entity) throws Exception {
        if (entity == null) {
            throw new Exception("update(null)");
        }

        String tableName = entity.getClass().getAnnotation(OrmEntity.class).table();
        Field idField = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(OrmColumnId.class))
                .findFirst().get();
        idField.setAccessible(true);
        Object idValue = idField.get(entity);

        StringBuilder queryBuilder = new StringBuilder("update " + tableName + " set ");

        Field[] columnFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(OrmColumn.class))
                .toArray(Field[]::new);

        for (Field field : columnFields) {
            field.setAccessible(true);
            String columnName = field.getAnnotation(OrmColumn.class).name();
            Object value = field.get(entity);
            if (field.getType().equals(String.class) && value != null) {
                value = "'" + value + "'";
            }
            queryBuilder.append(columnName).append(" = ").append(value).append(", ");
        }
        queryBuilder.replace(queryBuilder.length() - 2, queryBuilder.length(), "");

        queryBuilder.append(" where ")
                .append(idField.getName())
                .append(" = ")
                .append(idValue)
                .append(";");

        String sqlQuery = queryBuilder.toString();
        System.out.println("SQL query: " + sqlQuery);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
        }
    }

    String postgresType(Class<?> javaType, OrmColumn ormColumn) throws IllegalArgumentException {
        if (javaType.equals(String.class)) {
            return "varchar(" + ormColumn.length() + ")";
        } else if (javaType.equals(Integer.class)) {
            return "integer";
        } else if (javaType.equals(Double.class)) {
            return "double precision";
        } else if (javaType.equals(Boolean.class)) {
            return "boolean";
        } else if (javaType.equals(Long.class)) {
            return "bigint";
        }

        throw new IllegalArgumentException("ORM not support [" + javaType + "] type.");
    }
}
