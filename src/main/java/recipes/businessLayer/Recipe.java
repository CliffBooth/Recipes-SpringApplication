package recipes.businessLayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {

     @JsonIgnore
     @Id
     @GeneratedValue
     @Column(name = "id", updatable = false, nullable = false)
     private Long id;

     @NotBlank
     @Column(name = "category")
     private String category;

     @Column(name = "date")
     private LocalDateTime date;

     @NotBlank
     @Column(name = "name")
     private String name;


     @NotBlank
     @Column(name = "description")
     private String description;

     @NotEmpty
     @ElementCollection
     @CollectionTable(name = "ingredients", joinColumns = @JoinColumn(name = "id"))
     @Column(name = "ingredients")
     private List<String> ingredients = new ArrayList<>();

     @NotEmpty
     @ElementCollection
     @CollectionTable(name = "directions", joinColumns = @JoinColumn(name = "id"))
     @Column(name = "directions")
     private List<String> directions = new ArrayList<>();

     @JsonIgnore
     @Column(name = "author")
     private String email;
}


