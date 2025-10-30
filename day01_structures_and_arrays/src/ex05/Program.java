package ex05;

import java.util.Scanner;

public class Program {
    // выкручиваемся доступными структурами данных (Primitive types, String, arrays)
    private static String[] students = new String[10];
    private static int studentsCount = 0;

    /*
     * "Classes can be held any day of the week between 13:00 and 18:00."
     * ни слова про разделители между часом и минутами -> занятия будут начинаться в
     * XX:00
     * занятия могу начаться в 1, 2, 3, 4, 5 час дня (в 6 уже поздно начинать)
     */
    private static boolean[][] dayOfMonthAndTime = new boolean[31][6];

    private static int[][][] attendance = new int[10][31][6];
    private static final int ROW_SIZE = 9;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean statusOk = inputStudentNames(scanner);
        if (!statusOk) {
            System.out.println("Incorrect Input");
            scanner.close();
            System.exit(-1);
        }

        statusOk = fillTimetable(scanner);
        if (!statusOk) {
            System.out.println("Incorrect Input");
            scanner.close();
            System.exit(-1);
        }

        statusOk = recordAttendance(scanner);
        if (!statusOk) {
            System.out.println("Incorrect Input");
            scanner.close();
            System.exit(-1);
        }
        scanner.close();

        printTimetable();
    }

    private static boolean inputStudentNames(Scanner scanner) {
        boolean statusOk = true;

        while (studentsCount < 10 && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(".")) {
                break;
            }
            students[studentsCount++] = line;
        }

        if (studentsCount == 0) {
            statusOk = false;
        }

        return statusOk;
    }

    private static boolean fillTimetable(Scanner scanner) {
        boolean statusOk = true;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(".")) {
                break;
            }

            Scanner sc = new Scanner(line);
            int time = sc.nextInt();
            String dayOfWeek = sc.next();
            sc.close();

            if (time < 1 || time > 5) {
                System.out.printf("Wrong time. Entry [%s] will be passed\n", line);
                continue;
            }

            for (int date = 1; date <= 30; date++) {
                if (isDayOfThisWeek(date, dayOfWeek)) {
                    dayOfMonthAndTime[date][time] = true;
                }
            }
        }

        return statusOk;
    }

    private static boolean isDayOfThisWeek(int dayOfMonth, String dayOfWeek) {
        if (dayOfMonth < 1 || dayOfMonth > 30) {
            return false;
        }

        if (dayOfWeek.equals("MO") && (dayOfMonth % 7 == 0)) {
            return true;
        }

        if (dayOfWeek.equals("TU") && (dayOfMonth % 7 == 1)) {
            return true;
        }

        if (dayOfWeek.equals("WE") && (dayOfMonth % 7 == 2)) {
            return true;
        }

        if (dayOfWeek.equals("TH") && (dayOfMonth % 7 == 3)) {
            return true;
        }

        if (dayOfWeek.equals("FR") && (dayOfMonth % 7 == 4)) {
            return true;
        }

        if (dayOfWeek.equals("SA") && (dayOfMonth % 7 == 5)) {
            return true;
        }

        if (dayOfWeek.equals("SU") && (dayOfMonth % 7 == 6)) {
            return true;
        }

        return false;
    }

    private static boolean recordAttendance(Scanner scanner) {
        boolean statusOk = true;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(".")) {
                break;
            }

            Scanner sc = new Scanner(line);
            String name = sc.next();
            int time = sc.nextInt();
            int date = sc.nextInt();
            String status = sc.next();
            sc.close();

            int studentId = getStudentId(name);
            if (studentId == -1) {
                System.out.printf("Wrong student name. Entry [%s] passed\n", line);
                continue;
            }
            if (time < 1 || time > 5) {
                System.out.printf("Wrong time. Entry [%s] passed\n", line);
                continue;
            }
            if (date < 1 || date > 30) {
                System.out.printf("Wrong date. Entry [%s] passed\n", line);
                continue;
            }
            if (!status.equals("NOT_HERE") && !status.equals("HERE")) {
                System.out.printf("Wrong attendance. Entry [%s] passed\n", line);
                continue;
            }

            attendance[studentId][date][time] = status.equals("HERE") ? 1 : -1;
        }

        return statusOk;
    }

    private static int getStudentId(String name) {
        int id = -1;
        for (int i = 0; i < 10; i++) {
            if (name.equals(students[i])) {
                id = i;
                break;
            }
        }
        return id;
    }

    private static void printTimetable() {
        int i = 0, date = 1, time = 1;

        System.out.println();
        while (date <= 30) {
            int datePrev = date, timePrev = time;

            // шапка
            int j = 0;
            while (j < ROW_SIZE && date <= 30) {
                if (dayOfMonthAndTime[date][time]) {
                    if (j == 0) {
                        System.out.printf("%10s|", "");
                    }

                    System.out.printf("%d:00 %2s %2d|", time, getDayOfThisWeek(date), date);
                    j++;
                }

                time++;
                if (time > 5) {
                    time = 1;
                    date++;
                }
            }
            System.out.println();

            if (j == 0) {
                continue;
            }

            int dateNext = date, timeNext = time;

            // студенты / посещение
            for (int k = 0; k < studentsCount; k++) {
                date = datePrev;
                time = timePrev;
                j = 0;
                while (j < ROW_SIZE && date <= 30) {

                    if (dayOfMonthAndTime[date][time]) {
                        if (j == 0) {
                            System.out.printf("%10s|", students[k]);
                        }

                        if (attendance[k][date][time] == 0) {
                            System.out.printf("%10s|", "");
                        } else {
                            System.out.printf("%10d|", attendance[k][date][time]);
                        }
                        j++;
                    }

                    time++;
                    if (time > 5) {
                        time = 1;
                        date++;
                    }
                }

                System.out.println();
            }

            System.out.println();
            date = dateNext;
            time = timeNext;
        }
    }

    private static String getDayOfThisWeek(int dayOfMonth) {
        return switch (dayOfMonth % 7) {
            case 0 -> "MO";
            case 1 -> "TU";
            case 2 -> "WE";
            case 3 -> "TH";
            case 4 -> "FR";
            case 5 -> "SA";
            case 6 -> "SU";
            default -> "-";
        };
    }
}
