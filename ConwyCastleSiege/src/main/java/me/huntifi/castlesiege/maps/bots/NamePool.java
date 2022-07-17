package me.huntifi.castlesiege.maps.bots;

import java.util.ArrayList;
import java.util.Random;

public class NamePool {

    public static ArrayList<String> namepool = new ArrayList<>();

    public static Random random = new Random();

    public static boolean isRegistered = false;

    public static void registerNamePool() {

        if (!isRegistered) {

            namepool.add("Borneo");
            namepool.add("Alaric");
            namepool.add("Alban");
            namepool.add("Aldo");
            namepool.add("Ambrose");
            namepool.add("Anselm");
            namepool.add("Archibald");
            namepool.add("August");
            namepool.add("Baldwin");
            namepool.add("Balthasar");
            namepool.add("Cassian");
            namepool.add("Castellan");
            namepool.add("Chapman");
            namepool.add("Edmund");
            namepool.add("Florian");
            namepool.add("Gavin");
            namepool.add("Giles");
            namepool.add("Godfrey");
            namepool.add("Godric");
            namepool.add("Winifred");
            namepool.add("Ivar");
            namepool.add("Evio");
            namepool.add("Leopold");
            namepool.add("Lucian");
            namepool.add("Maxim");
            namepool.add("Milo");
            namepool.add("Nero");
            namepool.add("Xavius");
            namepool.add("Nepharius");
            namepool.add("Palmer");
            namepool.add("Neville");
            namepool.add("Severin");
            namepool.add("Peregrine");
            namepool.add("Rudolf");
            namepool.add("Sigrid");
            namepool.add("Violis");
            namepool.add("Lariam");
            namepool.add("Honorarius");
            namepool.add("Benedict");
            namepool.add("Barthomolew");
            namepool.add("Aragorn");
            namepool.add("Legolas");
            namepool.add("Gimly");
            namepool.add("Sam");
            namepool.add("Arthas");
            namepool.add("Uther");
            namepool.add("Therenas");
            namepool.add("Varian");
            namepool.add("Anduin");
            namepool.add("Frank");
            namepool.add("Guy");
            namepool.add("Don");
            namepool.add("Vanodon");
            namepool.add("Igor");
            namepool.add("Anatoly");
            namepool.add("Oliver");
            namepool.add("John");
            namepool.add("Steve");
            namepool.add("Caliny");
            namepool.add("Andrew");
            namepool.add("Charlie");
            namepool.add("Richard");
            namepool.add("Marco");
            namepool.add("Aize");
            namepool.add("Hugh");
            namepool.add("Jotaro");
            namepool.add("Dio");
            namepool.add("George");
            namepool.add("Josuke");
            namepool.add("Joseph");
            namepool.add("Tirion");
            namepool.add("Foldor");
            namepool.add("Ferox");
            namepool.add("Kyle");
            namepool.add("Bard");
            namepool.add("Björn");
            namepool.add("Cathasach");
            namepool.add("Charibert");
            namepool.add("Conrad");
            namepool.add("Cyprian");
            namepool.add("Daegal");
            namepool.add("Smeagol");
            namepool.add("Drogo");
            namepool.add("Dane");
            namepool.add("Dustin");
            namepool.add("Balin");
            namepool.add("Rockslide");
            namepool.add("Sasuri");
            namepool.add("Nagato");
            namepool.add("Wek");
            namepool.add("Decses");
            namepool.add("Hunt");
            namepool.add("Galileo");
            namepool.add("Grimwald");
            namepool.add("Jeremiah");
            namepool.add("Hildebald");
            namepool.add("Gregory");
            namepool.add("Arthur");
            namepool.add("Merlin");
            namepool.add("Hamlin");
            namepool.add("Randolf");
            namepool.add("Radagast");
            namepool.add("Wymond");
            namepool.add("Zemislav");
            namepool.add("Svend");
            namepool.add("Ricard");
            namepool.add("Pascal");
            namepool.add("Osric");
            namepool.add("Njal");
            namepool.add("Odel");
            namepool.add("Maurin");
            namepool.add("Lothar");
            namepool.add("Kenric");
            namepool.add("Lunden");
            namepool.add("Meccus");
            namepool.add("Dolin");
            namepool.add("Meccus");
            namepool.add("Mohammed");
            namepool.add("Murad");
            namepool.add("Ardin");
            namepool.add("Joneil");
            namepool.add("Farinus");
            namepool.add("Faramir");
            namepool.add("Boromir");

            isRegistered = true;
        }
    }


    public static String getRandomName() {

        return namepool.get(random.nextInt(namepool.size()));
    }



}
