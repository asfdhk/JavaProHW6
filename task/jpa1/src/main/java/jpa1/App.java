package jpa1;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add dish");
                    System.out.println("2: delete dish");
                    System.out.println("3: change price");
                    System.out.println("4: view dish");
                    System.out.println("5: filter price");
                    System.out.println("6: have actions");
                    System.out.println("7: 1kg");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addDish(sc);
                            break;
                        case "2":
                            deleteDish(sc);
                            break;
                        case "3":
                            changeDish(sc);
                            break;
                        case "4":
                            viewDish();
                            break;
                        case "5":
                            filterPrice(sc);
                            break;
                        case "6":
                            haveActions();
                            break;
                        case "7":
                            kg1();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void addDish(Scanner sc) {
        System.out.print("Enter name dish: ");
        String name = sc.nextLine();
        System.out.print("Enter price dish: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);
        System.out.print("Enter weight dish: ");
        String sWeight =sc.nextLine();
        int weight = Integer.parseInt(sWeight);
        System.out.print("Enter has action yes or no: ");
        String action = sc.nextLine();
        if (!action.equals("yes") && !action.equals("no")){
            System.out.print("Enter yes or no: ");
            action = sc.nextLine();
        }

        em.getTransaction().begin();
        try {
            Dish c = new Dish(name, price,weight,action);
            em.persist(c);
            em.getTransaction().commit();

            System.out.println(c.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void deleteDish(Scanner sc) {
        System.out.print("Enter dish id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Dish c = em.getReference(Dish.class, id);
        if (c == null) {
            System.out.println("Dish not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void changeDish(Scanner sc) {
        System.out.print("Enter dish name: ");
        String name = sc.nextLine();

        System.out.print("Enter new price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        Dish c = null;
        try {
            Query query = em.createQuery("SELECT x FROM Dish x WHERE x.name = :name", Dish.class);
            query.setParameter("name", name);

            c = (Dish) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Dish not found!");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }

        ///........

        em.getTransaction().begin();
        try {
            c.setPrice(price);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }


    private static void viewDish() {
        Query query = em.createQuery("SELECT c FROM Dish c", Dish.class);
        List<Dish> list = (List<Dish>) query.getResultList();

        for (Dish c : list)
            System.out.println(c);
    }

    private static void filterPrice(Scanner sc){
        Query query = em.createQuery("SELECT c FROM Dish c", Dish.class);
        List<Dish> list = (List<Dish>) query.getResultList();

        System.out.print("Enter max price: ");
        String sMaxPrice = sc.nextLine();
        int maxPrice = Integer.parseInt(sMaxPrice);
        System.out.print("Enter min price: ");
        String sMinPrice = sc.nextLine();
        int minPrice = Integer.parseInt(sMinPrice);

        list.stream()
                .filter(a->a.getPrice() >= minPrice && a.getPrice()<= maxPrice)
                .forEach(System.out::println);
    }

    private static void kg1(){
        List<Dish> list = new ArrayList<>();
        int sum = 0;
        while (sum<=100){
            Dish a = randomDish();
            sum+=a.getWeight();
            list.add(a);
        }
        for (Dish a : list){
            System.out.println(a);
        }
    }


    private static void haveActions(){
        Query query = em.createQuery("SELECT c FROM Dish c", Dish.class);
        List<Dish> list = (List<Dish>) query.getResultList();

        list.stream()
                .filter(n-> n.getAction().equals("yes"))
                .forEach(System.out::println);
    }

    static final Random RND = new Random();

    private static Dish randomDish(){
        Query query = em.createQuery("SELECT c FROM Dish c", Dish.class);
        List<Dish> list = (List<Dish>) query.getResultList();
        Dish[] arr = list.toArray(new Dish[0]);
        return arr[RND.nextInt(arr.length)];
    }

}


