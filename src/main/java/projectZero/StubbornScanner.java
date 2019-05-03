package projectZero;

import java.util.Scanner;

public class StubbornScanner {
	// private static Scanner sc = new Scanner(System.in);

	public static int scanInt(Scanner sc) {
		boolean haveInt = false;
		int scannedInt = -1;// eclipse demands it be initialized outside the while
		while (!haveInt) {
			if (sc.hasNextInt()) {
				scannedInt = sc.nextInt();
				haveInt = true;
				if (scannedInt < 0) {
					System.out.println("No negative numbers allowed");
					haveInt = false;
				}
			} else
				sc.next();
		}
		return scannedInt;
	}

	public static double scanDouble(Scanner sc) {
		boolean haveDouble = false;
		double scannedDouble = -1;// eclipse demands it be initialized outside the while
		while (!haveDouble) {
			if (sc.hasNextDouble()) {
				scannedDouble = sc.nextDouble();
				haveDouble = true;
				if (scannedDouble < 0) {
					System.out.println("No negative numbers allowed");
					haveDouble = false;
				}
			} else
				sc.next();
		}
		return scannedDouble;
	}

	public static int scanValidId(Scanner sc, int[] accountIds) {
		boolean isValid = false;
		int accountId = -1;
		while (!isValid) {
			accountId = StubbornScanner.scanInt(sc);
			for (int i : accountIds) {
				if (i == accountId) {
					isValid = true;
				}
			}
			if (isValid == false) {
				System.out.println("Enter the id of a open account you have access to");
			}
		}
		return accountId;
	}

}
