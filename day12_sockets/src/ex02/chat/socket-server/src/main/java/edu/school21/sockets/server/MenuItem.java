package edu.school21.sockets.server;

enum MenuItem {
    SIGH_IN, SIGN_UP, ROOM_CREATE, ROOM_CHOOSE, EXIT;

    public static String getNumberedMenuList(MenuItem[] itemsArr) {
        StringBuilder menuList = new StringBuilder();
        for (int i = 0; i < itemsArr.length; i++) {
            menuList.append(String.format("%d) %s", i + 1, itemsArr[i]));
            if (i < itemsArr.length - 1) {
                menuList.append("\n");
            }
        }
        return menuList.toString();
    }

    @Override
    public String toString() {
        switch (this) {
            case SIGH_IN:
                return "Sign in";
            case SIGN_UP:
                return "Sign up";
            case ROOM_CREATE:
                return "Create room";
            case ROOM_CHOOSE:
                return "Choose room";
            case EXIT:
                return "Exit";
            default:
                return "_";
        }
    }
}