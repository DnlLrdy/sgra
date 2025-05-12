package com.project.sgra.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parada {

    private String nombre;
    private Double latitud;
    private Double longitud;
    private Color color;

    public enum Color {
        BLUE("blue"),
        GOLD("gold"),
        RED("red"),
        GREEN("green"),
        ORANGE("orange"),
        YELLOW("yellow"),
        VIOLET("violet"),
        GREY("grey"),
        BLACK("black");

        private final String color;

        Color(String color) {
            this.color = color;
        }

        @JsonValue
        public String getColor() {
            return color;
        }

        @JsonCreator
        public static Color fromValue(String value) {
            for (Color c : Color.values()) {
                if (c.color.equalsIgnoreCase(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
