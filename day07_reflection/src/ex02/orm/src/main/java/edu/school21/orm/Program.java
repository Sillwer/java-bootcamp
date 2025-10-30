package edu.school21.orm;

import edu.school21.model.Product;
import edu.school21.model.User;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class Program {
    public static void main(String[] args) {
        EmbeddedDatabase db = null;
        try {
            db = new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .generateUniqueName(true)
                    .build();

            OrmManager ormManager = new OrmManager(db);

            System.out.println("\normManager INIT");
            ormManager.init(User.class);
            ormManager.init(Product.class);

            Object[] objects = {
                    new User(null, "Clark", "Kent", 19),
                    new User(null, "Lex", "Luter", 30),
                    new Product(null, "Super Komputker Asus Shmasus 3000", 999.99, 5),
                    new Product(null, "Headphones #2132", 999.99, 5)
            };

            System.out.println("\normManager SAVE");
            for (Object obj : objects) {
                ormManager.save(obj);
                System.out.println("    inserted: " + obj);
            }

            int someId = 1;

            System.out.println("\normManager FIND BY ID");
            User usr = ormManager.findById((long) someId, User.class);
            System.out.println("    found: " + usr);

            System.out.println("\normManager UPDATE");
            usr.setLastName(null);
            usr.setAge(null);
            ormManager.update(usr);
            User usrUpdated = ormManager.findById(usr.getId(), usr.getClass());
            System.out.println("    updated: " + usrUpdated);

            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
        } finally {
            if (db != null) {
                db.shutdown();
            }
        }
    }
}
