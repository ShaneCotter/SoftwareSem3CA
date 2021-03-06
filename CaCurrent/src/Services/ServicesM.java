package Services;

/**
 *
 * @author Shane Cotter X00131736 & Matthew Cleary X00130277
 */
import ex1.Route;
import ex1.Timetable;
import ex1.Bus;
import ex1.Fare;
import java.util.List;
import javax.persistence.*;

public class ServicesM {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CaTestPU");
    EntityManager em = emf.createEntityManager();

///////////////////////////QUERIES//////////////////////////////////////////////
///START OF USER MENU QUERIES///
    /*User Menu Option 1*/
 /*Also used to print all routes in Admin menu*/
    public void printAllRouteDetails() {

        Query query = em.createQuery("Select r From Route r");
        List<Route> list = query.getResultList();

        for (Route e : list) {
            System.out.println(e);
        }
    }

    /*Used to print all timetables in Admin menu*/
    public void printAllTimetables() {

        Query query = em.createQuery("Select t From Timetable t");
        List<Timetable> list = query.getResultList();

        for (Timetable t : list) {
            System.out.println(t);
        }
    }

    /*Used to print all buses in Admin menu*/
    public void printAllBuses() {

        Query query = em.createQuery("Select b From Bus b");
        List<Bus> list = query.getResultList();

        for (Bus b : list) {
            System.out.println(b);
        }
    }

    /*Used to print all fares in Admin menu*/
    public void printAllFares() {

        Query query = em.createQuery("Select f From Fare f");
        List<Fare> list = query.getResultList();

        for (Fare f : list) {
            System.out.println(f);
        }
    }

    /*User Menu Option 2*/
    public void printARoutesDetails(int routeNum) {

        Query query = em.createQuery("Select r From Route r Where r.route_num = " + routeNum);
        List<Route> list = query.getResultList();

        for (Route e : list) {
            System.out.println(e);
        }
    }

    /*User Menu Option 3*/
    public void printTimetable(int routeNum) {

        Query query = em.createQuery("Select t From Timetable t Where t.route = " + routeNum);
        List<Timetable> list = query.getResultList();

        for (Timetable t : list) {
            System.out.println(t);
        }
    }

    /*User Menu Option 4*/
    public void printBusesOnRoute(int routeNum) {

        Route r = em.find(Route.class, routeNum);
        r.printBuses();
    }

    /*User Menu Option 5*/
    public void printFares(int routeNum) {

        Query query = em.createQuery("Select f From Fare f Where f.route = " + routeNum);
        List<Fare> list = query.getResultList();

        for (Fare f : list) {
            System.out.println(f);
        }
    }
//////END OF USER QUERIES///////

//////START OF ROUTE METHODS//////
    public int findRouteID(String firstStop, String destination) {

        int routeID = 0;

        Query query = em.createQuery("SELECT r.id FROM Rote r "
                + "WHERE r.last_stop=:value "
                + "AND r.first_stop=:value2");
        query.setParameter("value", firstStop);
        query.setParameter("value2", destination);
        try {
            routeID = (int) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Nothing Found " + e.getMessage());
        }
        return routeID;
    }

    public boolean findRoute(int route_numIn) {
        boolean found = false;

        Query q = em.createNativeQuery("SELECT r.journey_time \n"
                + "From Route r \n"
                + "WHERE r.route_num = " + route_numIn);

        List<Route> results = q.getResultList();

        if (!results.isEmpty()) {
            found = true;
        }
        return found;
    }

    public void addFareToRoute(int routeNumIn, Fare f) {
        Route r = em.find(Route.class, routeNumIn);
        r.setFare(f);
        em.persist(r);
    }

    public void addBusToRoute(int routeNumIn, Bus b) {
        Route r = em.find(Route.class, routeNumIn);
        b.addRoute(r);
        em.persist(b);
    }

    /*All Route options displayed after selecting admin menu option 1*/
 /*Route option 1*/
    public void removeRoute(int id) {

        Route r = em.find(Route.class, id);
        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
    }

    /*Route option 2*/
    public Route createRoute(int route_num, String first_stop, String last_stop, String journey_time, int numStops) {

        em.getTransaction().begin();
        Route r = new Route(route_num, first_stop, last_stop, journey_time, numStops);
        em.persist(r);
        em.getTransaction().commit();
        return r;
    }

    /*Route option 3*/
    public void updateRouteJourneyTime(int id, String newJourneyTime) {
        em.getTransaction().begin();
        Route r = em.find(Route.class, id);
        r.setJourney_time(newJourneyTime);
        em.getTransaction().commit();
    }

    //////START OF TIMETABLE METHODS//////
    public boolean findTimetable(int timetableNum) {
        boolean found = false;

        Query q = em.createNativeQuery("SELECT t.frequency \n"
                + "From Timetable t \n"
                + "WHERE t.timetable_id = " + timetableNum);

        List<Timetable> results = q.getResultList();

        if (!results.isEmpty()) {
            found = true;
        }
        return found;
    }

    /*All Timetable Options displayed after selecting admin menu option 2*/
 /*Timetable option 1*/
    public void removeTimetable(int id) {

        Timetable t = em.find(Timetable.class, id);
        em.getTransaction().begin();
        em.remove(t);
        em.getTransaction().commit();
    }

    /*Timetable option 2*/
    public Timetable createTimetable(int timetable_id, int route_num, String timetable_type, String first_journey, String last_journey, String frequency) {

        em.getTransaction().begin();
        Timetable t = new Timetable(timetable_id, route_num, timetable_type, first_journey, last_journey, frequency);
        em.persist(t);

        Route r = em.find(Route.class, route_num);
        r.addTimetable(t);

        em.getTransaction().commit();
        return t;
    }

    /*Timetable option 3*/
    public void updateTimetableFrequency(int id, String newFrequency) {
        em.getTransaction().begin();
        Timetable t = em.find(Timetable.class, id);
        t.setFrequency(newFrequency);
        em.getTransaction().commit();
    }

    //////START OF BUS METHODS//////
    public boolean findBus(int bus_id) {
        boolean found = false;

        Query q = em.createNativeQuery("SELECT b.bus_type \n"
                + "From Bus b \n"
                + "WHERE b.bus_id = " + bus_id);

        List<Bus> results = q.getResultList();

        if (!results.isEmpty()) {
            found = true;
        }
        return found;
    }

    /*All Bus Options displayed after selecting admin menu option 3*/
 /*Bus option 1*/
    public void removeBus(int id) {

        Bus b = em.find(Bus.class, id);
        em.getTransaction().begin();
        em.remove(b);
        em.getTransaction().commit();
    }

    /*Bus option 2*/
    public Bus createBus(int bus_id, int capacity, String bus_type) {

        em.getTransaction().begin();
        Bus b = new Bus(bus_id, capacity, bus_type);
        em.persist(b);
        em.getTransaction().commit();
        return b;
    }

    /*Bus option 3*/
    public void updateBusType(int id, String newType) {
        em.getTransaction().begin();
        Bus b = em.find(Bus.class, id);
        b.setBus_type(newType);
        em.persist(b);
        em.getTransaction().commit();
    }

//////START OF FARE METHODS//////
    public boolean findFare(int route_num) {
        boolean found = false;

        Query q = em.createNativeQuery("SELECT f.adult_fare \n"
                + "From Fare f \n"
                + "WHERE f.route = " + route_num);

        List<Fare> results = q.getResultList();

        if (!results.isEmpty()) {
            found = true;
        }
        return found;
    }

    /*All Fare options displayed after selecting admin menu option 1*/
 /*Fare option 1*/
    public void removeFare(int id) {

        Fare f = em.find(Fare.class, id);
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();
    }

    /*Fare option 2*/
    public Fare createFare(int route_num, double childFare, double studentFare, double adultFare, double oapFare) {

        em.getTransaction().begin();
        Fare f = new Fare(route_num, childFare, studentFare, adultFare, oapFare);
        em.persist(f);
        em.getTransaction().commit();
        return f;
    }

    /*Below 4 update's all used in Fare option 3*/
    public void updateChildFare(int id, double newFare) {
        em.getTransaction().begin();
        Fare f = em.find(Fare.class, id);
        f.setChildFare(newFare);
        em.persist(f);
        em.getTransaction().commit();
    }

    public void updateStudentFare(int id, double newFare) {
        em.getTransaction().begin();
        Fare f = em.find(Fare.class, id);
        f.setStudentFare(newFare);
        em.persist(f);
        em.getTransaction().commit();
    }

    public void updateAdultFare(int id, double newFare) {
        em.getTransaction().begin();
        Fare f = em.find(Fare.class, id);
        f.setAdultFare(newFare);
        em.persist(f);
        em.getTransaction().commit();
    }

    public void updateOapFare(int id, double newFare) {
        em.getTransaction().begin();
        Fare f = em.find(Fare.class, id);
        f.setOapFare(newFare);
        em.persist(f);
        em.getTransaction().commit();
    }

/////////////////////////END QUERIES////////////////////////////////////////////
}
