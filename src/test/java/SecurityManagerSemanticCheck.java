import java.security.Permission;

public class SecurityManagerSemanticCheck extends SecurityManager {

  @Override
  public void checkExit(int status) {
    // Only catches semantic errors
    if (status == 200) {
      throw new SecurityException();
    }
  }

  @Override
  public void checkPermission(Permission perm) {
    // Allow other activities by default
  }

}