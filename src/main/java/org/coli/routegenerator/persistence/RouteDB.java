package org.coli.routegenerator.persistence;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static org.hibernate.type.SqlTypes.JSON;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDB implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String routeKey; // 'key' is a reserved keyword in H2

    @JdbcTypeCode(JSON)
    private List<List<String>> routes;

    private int currentIndex;
}
