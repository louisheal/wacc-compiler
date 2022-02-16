import java.security.Permission;

public class MySecurityManager extends SecurityManager {

  @Override
  public void checkExit(int status) {
    // Since semantic and syntax errors send exit status of 100/200, we can catch them in the test
    // to send an error out for the specific tests
    if (status == 100 || status == 200) {
      throw new SecurityException();
    }
  }

  @Override
  public void checkPermission(Permission perm) {
    // Allow other activities by default
  }

}
