package ex05;

public class UserIdsGenerator {
    private static Integer usersCount = 0;

    static Integer generateId() {
        return ++usersCount;
    }
}

////  пока не вижу смысла так мудрить
//public class UserIdsGenerator {
//    private static Integer usersCount;
//    private static UserIdsGenerator userIdsGenerator;
//
//    UserIdsGenerator() {
//    }
//
//    static UserIdsGenerator getInstance() {
//        if (userIdsGenerator == null) {
//            userIdsGenerator = new UserIdsGenerator();
//            UserIdsGenerator.usersCount = 0;
//        }
//        return userIdsGenerator;
//    }
//
//    Integer generateId() {
//        return ++usersCount;
//    }
//}
