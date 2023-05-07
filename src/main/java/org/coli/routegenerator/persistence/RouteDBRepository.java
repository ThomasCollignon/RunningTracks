package org.coli.routegenerator.persistence;

import org.coli.routegenerator.exception.RTException;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RouteDBRepository {

    private static final String FILE = "./src/main/resources/routes-database";

    private List<RouteDB> dbContent = new ArrayList<>();

    public RouteDB save(RouteDB routeDB) {
        String routeKey = routeDB.getRouteKey();
        findById(routeKey).ifPresentOrElse(routeDB1 -> routeDB1.setCurrentIndex(routeDB.getCurrentIndex()),
                                           () -> dbContent.add(routeDB));
        return routeDB;
    }

    public Optional<RouteDB> findById(String routeKey) {
        return dbContent.stream()
                        .filter(routeDB -> routeDB.getRouteKey().equals(routeKey))
                        .findFirst();
    }

    public void saveAll(List<RouteDB> routes) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE))) {
            objectOutputStream.writeObject(routes);
        } catch (IOException e) {
            throw new RTException(e.getMessage());
        }
    }

    public List<RouteDB> findAll() {
        return dbContent;
    }

    @EventListener(ContextRefreshedEvent.class)
    @SuppressWarnings("unchecked")
    void getDBContentAtStartup() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE))) {
            dbContent = new ArrayList<>((List<RouteDB>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RTException(e.getMessage());
        }
    }
}
