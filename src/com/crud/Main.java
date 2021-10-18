package com.crud;
import java.util.*;
import CRUD.Operasi;
import CRUD.Util;

import static java.lang.System.out;

public class Main {

    static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws Exception{

        String inputUser;
        boolean lanjut = true;

        while(lanjut) {

            Util.clearConsole();
            out.println("\nDatabase Buku\n");
            out.println("1.\tLihat Seluruh Buku");
            out.println("2.\tCari Buku");
            out.println("3.\tTambah Buku");
            out.println("4.\tEdit Buku");
            out.println("5.\tHapus Buku");

            out.print("\nMasukkan Pilihan Anda : ");
            inputUser = scan.nextLine();
            out.println("==============================================================");

            switch (inputUser) {
                case "1":
                    out.println("\t\t\t\t\tLihat Seluruh Buku");
                    out.println("==============================================================\n");
                    Operasi.tampilData();
                    break;
                case "2":
                    out.println("\t\t\t\t\tCari Buku");
                    out.println("==============================================================\n");
                    Operasi.getData();
                    break;
                case "3":
                    out.println("\t\t\t\t\tTambah Buku");
                    out.println("==============================================================\n");
                    Operasi.addData();
                    break;
                case "4":
                    out.println("\t\t\t\t\tEdit Buku");
                    out.println("==============================================================\n");
//                    editDataByRayazka();
                    Operasi.editDataByKelasTerbuka();
                    break;
                case "5":
                    out.println("\t\t\t\t\tHapus Buku");
                    out.println("==============================================================\n");
                    Operasi.deleteData();
                    break;
                default:
                    out.println("Pilihan yang anda masukkan tidak valid\nMasukkan Pilihan dari [1-5]\n");
            }
            lanjut = Util.getYes("Apakah anda ingin melanjutkan");

        }

    }
}
