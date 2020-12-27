package by.bstu.svs.db.lr10.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {
    private Integer groupId;
    private Integer course;
    private String faculty;
    private String name;
    private Integer headId;

    @Override
    public String toString() {
        return course + "-" + faculty + "-" + name;
    }
}
