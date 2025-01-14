package guru.qa.niffler.data.entity.spend;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class CategoryEntity implements Serializable {
  private UUID id;
  private String name;
  private String username;
  private boolean archived;
}
