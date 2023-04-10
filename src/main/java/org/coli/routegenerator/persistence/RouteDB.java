package org.coli.routegenerator.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.List;

import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDB {

    @Id
    private String routeKey; // 'key' is a reserved keyword in H2

    @JdbcTypeCode(JSON)
    private List<List<String>> routes;

    private int currentIndex;
}
