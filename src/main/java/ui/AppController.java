package ui;

import core.User;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import json.CashFlowPersistence;

/**
 * Class that decides what type of access the other controllers will get.
 */
public class AppController {

  private static final String baseUri = "http://localhost:8999/user/";

  @FXML
  String remote;

  @FXML
  String page;

  @FXML
  DetailsController detailsController;
  
  @FXML
  CashFlowController mainPageController;

  private CashFlowAccess cashFlowAccess;

  private CashFlowPersistence cfp;

  /**
   * The method tries to load user from the saved file. If no user is found, a default user is
   * created.
   *
   * @return the user
   */
  private User getInitialUser() {
    User initialUser = null;
    if (cfp != null) {
      try {
        initialUser = cfp.loadUser(DirectAccess.LOCALSAVEFILE);
      } catch (IOException e) {
        System.err.println("Fikk ikke lest inn lagret bruker");
      }
    }
    if (initialUser == null) {
      initialUser = new User(123456);
    }
    return initialUser;
  }

  /**
   * Initializes the controller.
   */
  @FXML
  public void initialize() {
    cfp = new CashFlowPersistence();
    cashFlowAccess = new DirectAccess(getInitialUser(), DirectAccess.LOCALSAVEFILE);
    if (remote.equals("false")) {
      if (page.equals("main")) {
        mainPageController.setCashFlowAccess(cashFlowAccess);
      } else if (page.equals("details")) {
        detailsController.setCashFlowAccess(cashFlowAccess);
      }
    } else if (remote.equals("true")) {
      try {
        cashFlowAccess = new RemoteAccess(new URI(baseUri));
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
      if (page.equals("main")) {
        mainPageController.setCashFlowAccess(cashFlowAccess);
      } else if (page.equals("details")) {
        detailsController.setCashFlowAccess(cashFlowAccess);
      }
    }
  }
}
