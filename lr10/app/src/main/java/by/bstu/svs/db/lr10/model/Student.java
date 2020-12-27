package by.bstu.svs.db.lr10.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer studentId;
    private Integer groupId;
    private String name;

    @Override
    public String toString() {
        return studentId + ". " + name;
    }
}
