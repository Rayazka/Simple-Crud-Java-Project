package CRUD;

import java.io.*;
import java.time.Year;
import java.util.Scanner;
import java.util.StringTokenizer;

import static java.lang.System.out;

public class Operasi {
    static final Scanner scan = new Scanner(System.in);


    //    No 1
    public static void tampilData() throws IOException {

        FileReader reader = new FileReader("database.txt");
        BufferedReader buffReader = new BufferedReader(reader);

        int i = 0;
        String dataStr = buffReader.readLine();

        out.println("No - Tahun - Nama Pengarang - Penerbit - Judul Buku");
        out.println("-------------------------------------------------------------------");

        while(dataStr != null) {
            i++;
            StringTokenizer dataToken = new StringTokenizer(dataStr,",");

            dataToken.nextToken();
            out.print(i);
            out.print(" - " + dataToken.nextToken());
            out.print(" - " + dataToken.nextToken());
            out.print(" - " + dataToken.nextToken());
            out.print(" - " + dataToken.nextToken());
            out.println();

            dataStr = buffReader.readLine();
        }
        out.println("-------------------------------------------------------------------");
    }

    //    No 2
    public static void getData() throws Exception {

//      Membaca ada atau tidak database
        try {
            File file = new File("database.txt");
        } catch(Exception e){
            System.err.println("\nDatabase tidak ditemukan");
            return;
        }

        //      Mengambil keyword user dari Scanner
        String inputUser;

        out.print("Masukkan keyword : ");
        inputUser = scan.nextLine();
        String[] arrKeywords = inputUser.split("\\s+");
        for(int i = 0; i < arrKeywords.length; i++){
            out.println("\nHasil pencarian dari " + arrKeywords[i] + "\n");
        }
        //      Mengabil keyword dari database dari keyword yang dimasukkan oleh user
        cekBuku(arrKeywords,true);

    }
    public static boolean cekBuku(String[] arrKeywords,boolean display) throws IOException {

//        Mengambil data dari database
        FileReader reader = new FileReader("database.txt");
        BufferedReader buff = new BufferedReader(reader);

        String data = buff.readLine();
        boolean isExist = false;

//        Jika display = true
        if(display) {
            out.println("No - Tahun - Nama Pengarang - Penerbit - Judul Buku");
            out.println("-------------------------------------------------------------------");
        }

//        Membuat perulangan hingga data tidak tersedia
        while(data != null){
            isExist = true; //mengubah isExist menjadi true

//
            for(String keyword : arrKeywords){
//                  Melooping untuk mencari tahu apakah keyword sudah ada di database
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

//            Jika data ada di database
            if(isExist){
                if(display){
                    StringTokenizer token = new StringTokenizer(data,",");

                    token.nextToken();
                    out.println("tahun\t\t: " + token.nextToken());
                    out.println("Penulis\t: " + token.nextToken());
                    out.println("Penerbit\t: " + token.nextToken());
                    out.println("Judul Buku\t: " + token.nextToken());

                } else{
                    break;
                }
            }
            data = buff.readLine();
        }
        return isExist;

    }

    //    No 3
    public static void addData() throws IOException{

        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

//        Mengambil input dari user
        Scanner terminalInput = new Scanner(System.in);
        String penulis, judul, penerbit, tahun;

        System.out.print("masukan nama penulis: ");
        penulis = terminalInput.nextLine();
        System.out.print("masukan judul buku: ");
        judul = terminalInput.nextLine();
        System.out.print("masukan nama penerbit: ");
        penerbit = terminalInput.nextLine();
        System.out.print("masukan tahun terbit, format=(YYYY): ");
        tahun = ambilTahun();

//        cek buku di database
        String[] keywords = {tahun+","+penulis+","+penerbit+","+judul};

        boolean isExist = cekBuku(keywords,false);

        if(!isExist){
            int noEntry = nomorEntry(penulis,tahun) + 1;

            String penulisTS = penulis.replaceAll("\\s+","");
            String primaryKey = penulisTS+"_"+tahun+"_"+noEntry;

            out.println("\nData yang anda masukkan adalah");
            out.println("-----------------------------------");
            out.println("Primary Key\t: " + primaryKey);
            out.println("Tahun\t\t: " + tahun);
            out.println("Penulis\t\t: " + penulis);
            out.println("Judul\t\t: " + judul);
            out.println("Penerbit\t: " + penerbit);

            boolean adding = Util.getYes("Apakah anda yakin ingin menambahkan");

            if(adding){
                bufferedOutput.write(primaryKey + "," + tahun + ","+ penulis +"," + penerbit + ","+judul);
                bufferedOutput.newLine();
                bufferedOutput.flush();
                out.println("--------Data Sudah Ditambahkan--------");
            }
        } else{
            out.println("Buka yang anda masukkan sudah tersedia");
            cekBuku(keywords,true);
        }

        bufferedOutput.close();
    }
    public static int nomorEntry(String penulis, String tahun) throws IOException{
        FileReader reader = new FileReader("database.txt");
        BufferedReader buffReader = new BufferedReader(reader);

        int entry = 0;
        String dataStr = buffReader.readLine();
        Scanner dataScanner;
        String primaryKey;

        while(dataStr != null){
            dataScanner = new Scanner(dataStr);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            penulis.replaceAll("\\s","");

            if(penulis.equalsIgnoreCase(dataScanner.next()) && tahun.equalsIgnoreCase(dataScanner.next())){
                entry = dataScanner.nextInt();
            }
            dataStr = buffReader.readLine();
        }
        return entry;
    }
    public static String ambilTahun() throws IOException{
        boolean tahunValid = false;
        Scanner terminalInput = new Scanner(System.in);
        String tahunInput = terminalInput.nextLine();
        while(!tahunValid){
            try {
                Year.parse(tahunInput);
                tahunValid = true;
            } catch (Exception e){
                out.println("Format tahun salah, format (YYYY)");
                out.print("Silahkan masukkan kembali : ");
                tahunInput = terminalInput.nextLine();
                tahunValid = false;

            }
        }
        return tahunInput;
    }

//  No 4
    //    Code by me
public static void editDataByRayazka() throws IOException{

//          Class untuk menulis data di temporary file
    File tempFile = new File("tempFile.txt");
    FileWriter fw = new FileWriter(tempFile,true);
    BufferedWriter bw = new BufferedWriter(fw);
    PrintWriter pw = new PrintWriter(bw);

//            Class untuk membaca database
    File databaseFile = new File("database.txt");
    FileReader fr = new FileReader(databaseFile);
    BufferedReader br = new BufferedReader(fr);

//            Class untuk mengambil input user
    tampilData(); //Menampilkan data terlebih dahulu
    Scanner jawaban = new Scanner(System.in);
    out.print("Masukkan Nomor Data Buku yang Ingin Diubah : ");
    int noUser = jawaban.nextInt(); //Menanyakan kepada user buku yang mana yang ingin di ubah

//              Membaca data dari database
    String dataStr = br.readLine();

//            Me-looping untuk membaca semua isi buku
    int jumlahBuku = 0;
    while(dataStr != null){

        boolean isEdit = false;
        jumlahBuku++;
//                Membuat StringTokenizer untuk menampilkan data
        StringTokenizer strToken = new StringTokenizer(dataStr,",");

//                Membuat kondisi jika no yang dipilih user valid
        if(noUser == jumlahBuku){
            out.println("\n---------------DATA BUKU---------------");
            out.println("primary key\t: " + strToken.nextToken());
            out.println("Tahun\t\t: " + strToken.nextToken());
            out.println("Pengarang\t: " + strToken.nextToken());
            out.println("Penerbit\t: " + strToken.nextToken());
            out.println("Judul Buku\t: " + strToken.nextToken());
            out.println("------------------------------------------");
            isEdit = Util.getYes("Apakah anda yakin ingin mengubah");
        }
//                Jika user setuju untuk mengubah data
        if(isEdit){
            String format,tahun,pengarang,penerbit,judulBuku,entryData,dataBuku;

            out.print("Masukkan pengarang : ");
            pengarang = scan.nextLine();

            out.print("Masukkan penerbit : ");
            penerbit = scan.nextLine();

            out.print("Masukkan judul buku : ");
            judulBuku = scan.nextLine();

            out.print("Masukkan tahun terbit : ");
            tahun = scan.nextLine();

            out.println("-------------------Data Sudah di Ubah-------------------");

//                    Mengambil no entry data yang ingin diubah
            Scanner dataScanner;
            String dataScan;
            dataScanner = new Scanner(dataStr);
            dataScanner.useDelimiter(",");
            dataScan = dataScanner.next();
            dataScanner = new Scanner(dataScan);
            dataScanner.useDelimiter("_");
            dataScanner.next();
            dataScanner.next();

            entryData = dataScanner.next();
            format = pengarang.replaceAll("\\s","") + "_" + tahun + "_" +  entryData;

            dataBuku = format + "," + tahun + "," + pengarang + "," + penerbit + "," + judulBuku ;

//                    Menulis dan memasukkan data yang telah diubah ke dalam temp file
            pw.println(dataBuku.toLowerCase());

        } else{
//                    Menulis dan memasukkan data data ke dalam temp file
            bw.write(dataStr);
            bw.newLine();

        }

        dataStr = br.readLine();
    }

//            Memasukkan data-data baru ke temporary file
    bw.flush();
    pw.flush();

//            Closing
    pw.close();
    bw.close();
    fw.close();
    fr.close();
    br.close();

//            Menghapus database lama dan mengubah temp file menjadi database utama
    System.gc();
    databaseFile.delete();
    tempFile.renameTo(databaseFile);
}

    //    Code by Kelas Terbuka
    public static void editDataByKelasTerbuka() throws IOException {
//        Mengambil data dari database
        File database = new File("database.txt");
        FileReader dbReader = new FileReader(database);
        BufferedReader bfReader = new BufferedReader(dbReader);

//        Membuat database sementara
        File tempFile = new File("temp.txt");
        FileWriter wrTemp = new FileWriter(tempFile);
        BufferedWriter bfWriter = new BufferedWriter(wrTemp);

//        Menampilkan data buku
        out.println("----------------------List Buku----------------------");
        tampilData();

//        Mengambil input dari user
        Scanner terminalScanner = new Scanner(System.in);
        out.print("Masukkan no data yang ingin anda ubah : ");
        int inputUser = terminalScanner.nextInt();

//        Menampilkan data yang dipilih oleh user
        String dataStr = bfReader.readLine();
        int numberEntry = 0;
        while(dataStr != null) {
            numberEntry++;
            StringTokenizer token = new StringTokenizer(dataStr, ",");

//            Jika pilihan user sama dengan numberEntry
            if (numberEntry == inputUser) {
                token.nextToken();

                out.println("\n-----Data Buku-----");
                out.println("Tahun\t\t: " + token.nextToken());
                out.println("Penlis\t\t: " + token.nextToken());
                out.println("Penerbit\t: " + token.nextToken());
                out.println("Judul Buku\t: " + token.nextToken());

//                Update data
                String[] fieldData = {"tahun","penulis","penerbit","judul buku"};
                String[] tempData = new String[fieldData.length];

                token = new StringTokenizer(dataStr, ",");
                String originalData = token.nextToken();

                for (int i = 0; i < fieldData.length; i++) {
                    boolean isUpdate = Util.getYes("Apakan anda ingin mengubah " + fieldData[i]);

                    originalData = token.nextToken();
                    if(isUpdate){
//                        Mengambil input user
                        terminalScanner = new Scanner(System.in);
                        out.print("Masukkan " + fieldData[i] + " baru : ");
                        tempData[i] = terminalScanner.nextLine().toLowerCase();
                    }
                    else{
                        tempData[i] = originalData; //Hanya meng-copy saja
                    }
                }

//                Menampilkan data yang sudah di update

                token = new StringTokenizer(dataStr, ",");
                token.nextToken();
                out.println("\n-----Data Buku yang sudah Diubah-----");
                out.println("Tahun\t\t: " + token.nextToken() + " --> " + tempData[0]);
                out.println("Penlis\t\t: " + token.nextToken() + " --> " + tempData[1]);
                out.println("Penerbit\t: " + token.nextToken()+ " --> " + tempData[2]);
                out.println("Judul Buku\t: " + token.nextToken()+ " --> " + tempData[3]);

                boolean isUpdate = Util.getYes("Apakah anda yakin ingin mengubahnya");

                if(isUpdate){

                    boolean isExist = cekBuku(tempData,false);

                    if(isExist){
                        out.println("Data yang anda masukkan sudah tersedia di database");
                        bfWriter.write(dataStr);
                    } else{

                        String tahun = tempData[0];
                        String penulis = tempData[1];
                        String penerbit = tempData[2];
                        String judul = tempData[3];

//                        Membuat primary key
                        int noEntry = nomorEntry(penulis,tahun);
                        String penulisTS = penulis.replaceAll("\\s+","");
                        String primaryKey = penulisTS+"_"+tahun+"_"+noEntry;

//                        menulis ke database
                        bfWriter.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                    }

                }else{
//                Copy data
                    bfWriter.write(dataStr);
                }

            } else{
//                Copy data
                bfWriter.write(dataStr);
            }
            bfWriter.newLine();

            dataStr = bfReader.readLine();
        }

        bfWriter.flush();

        bfWriter.close();
        wrTemp.close();
        bfReader.close();
        dbReader.close();
        System.gc();
        database.delete();
        tempFile.renameTo(database);

    }

    //  No 5
    public static void deleteData() throws Exception {

//       Membaca file original
        File dbFile = new File("database.txt");
        FileReader readerDb = new FileReader(dbFile);
        BufferedReader bfInput = new BufferedReader(readerDb);

//        Membuat temporary file
        File tempFile = new File("tempFile.txt");
        FileWriter writeTemp = new FileWriter(tempFile);
        BufferedWriter tempWrite = new BufferedWriter(writeTemp);

//        Mengambil input user untuk memilih data yang ingin dihapus
        tampilData();
        out.print("Masukkan nomor yang ingin anda hapus : ");
        int numberDelete = scan.nextInt();

        int entryData = 0;
        String dataStr = bfInput.readLine();
        boolean isValid = false;
        while(dataStr != null){
            entryData++;
            boolean isDelete = false;
            StringTokenizer strToken = new StringTokenizer(dataStr,",");

            if(entryData == numberDelete){
                out.println("\n\t\t\t\tData Buku");
                out.println("------------------------------------------");
                out.println("Primary Key\t: " + strToken.nextToken());
                out.println("Tahun\t\t: " + strToken.nextToken());
                out.println("Pengarang\t: " + strToken.nextToken());
                out.println("Penerbit\t: " + strToken.nextToken());
                out.println("Judul Buku\t: " + strToken.nextToken());
                out.println("------------------------------------------");


                isDelete = Util.getYes("Apakah anda yakin ingin menghapus");
                isValid = true;
            }

            if(isDelete){
//                Skip file yang dipilih untuk dipindahkan ke file temporary
                out.println("--------------Data sudah dihapus--------------");
            } else{
//                Memindahkan semua file kecuali file yang dipilih user ke temporary
                tempWrite.write(dataStr);
                tempWrite.newLine();
            }
            dataStr = bfInput.readLine();
        }
        if(!isValid){
            System.err.println("\t\tBuku tidak ditemukan!");
        }

        tempWrite.flush();

        tempWrite.close();
        writeTemp.close();
        bfInput.close();
        readerDb.close();
        System.gc();

//        Menghapus database sebelumnya
        dbFile.delete();
//        Merename tempFile untuk menjadi database utama
        tempFile.renameTo(dbFile);

    }


}

