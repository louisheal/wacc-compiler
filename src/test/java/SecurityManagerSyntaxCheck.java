import java.security.Permission;

public class SecurityManagerSyntaxCheck extends SecurityManager {

  @Override
  public void checkExit(int status) {
    // This will only catch syntax errors
    if (status == 100) {
      throw new SecurityException();
    }
  }

  @Override
  public void checkPermission(Permission perm) {
    // Allow other activities by default
  }

}