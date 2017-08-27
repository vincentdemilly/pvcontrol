package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import sun.plugin2.jvm.RemoteJVMLauncher;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class Controller {

    @FXML
    Button btn1;

    // Create the JobStatus Label
    @FXML
    javafx.scene.control.Label jobStatus = new javafx.scene.control.Label();

    @FXML
    VBox Vb;

    @FXML
    public void loginButtonClicked() throws IOException {

        Stage stage = (Stage) Vb.getScene().getWindow();
        //stage.setScene(new Scene());
        stage.show();

        Printer printer = Printer.getDefaultPrinter();
        System.out.println(printer);
        PageLayout pageLayout = printer.getDefaultPageLayout();
        System.out.println("PageLayout: " + pageLayout);

        // Printable area
        double pWidth = pageLayout.getPrintableWidth();
        double pHeight = pageLayout.getPrintableHeight();
        System.out.println("Printable area is " + pWidth + " width and "
                + pHeight + " he                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ight.");

        // Node's (Image) dimensions
        double nWidth = Vb.getBoundsInParent().getWidth();
        double nHeight = Vb.getBoundsInParent().getHeight();
        System.out.println("Node's dimensions are " + nWidth + " width and "
                + nHeight + " height");

        // How much space is left? Or is the image to big?
        double widthLeft = pWidth - nWidth;
        double heightLeft = pHeight - nHeight;
        System.out.println("Width left: " + widthLeft
                + " height left: " + heightLeft);

        // scale the image to fit the page in width, height or both
        double scale = 0;

        if (widthLeft < heightLeft) {
            scale = pWidth / nWidth;
        } else {
            scale = pHeight / nHeight;
        }

        // preserve ratio (both values are the same)
        Vb.getTransforms().add(new Scale(scale, scale));

        // after scale you can check the size fit in the printable area
        double newWidth = Vb.getBoundsInParent().getWidth();
        double newHeight = Vb.getBoundsInParent().getHeight();
        System.out.println("New Node's dimensions: " + newWidth
                + " width " + newHeight + " height");

        printSetup(Vb, stage);

        double vi = nHeight / newHeight;
        System.out.println(vi);

        Vb.getTransforms().add(new Scale(vi, vi));


    }

    private Boolean printSetup(Node node, Stage owner) {
        Boolean re=false;
        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();

        // Show the print setup dialog
        boolean proceed = job.showPrintDialog(owner);

        if (proceed) {
            print(job, node);
            re=true;
        }
        return re;
    }

    private void print(PrinterJob job, Node node) {
        // Print the node
        boolean printed = job.printPage(node);

        if (printed) {
            job.endJob();
        }
    }

    //Controller pour l'onglet File, sous section "nouveau"
    @FXML
    public void newOnClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Sample.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("PV CONTROL");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    TextField case0, case1, case10, case11;


    @FXML
    public void TestCoulour() throws SQLException {

        if (!case0.getText().isEmpty()) {
            for (Node node : grid2.getChildren()) {

                //gère le cas ou on efface le contenu des textfields filles
                if (node instanceof TextField && ((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 1){
                    (node).setStyle("-fx-background-color: whitesmoke;");
                }

                //gère le cas ou on tape un contenu identique dans les textfields filles par rapport au textfield mère
                if (node instanceof TextField && !((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 1 && ((TextField) node).getText().equals(case0.getText())) {
                    //la fonction de correspondance ira ici
                    (node).setStyle("-fx-background-color: whitesmoke;");
                    case0.setStyle("-fx-background-color: whitesmoke;");
                }

                if(!fruitCombo1.getSelectionModel().isEmpty() && node instanceof TextField && !((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 1 && !((TextField) node).getText().equals(case0.getText())){
                    System.out.println("ARMED ???");


                    ObservableList list = fruitCombo1.getItems();
                    System.out.println(list);

                    String value = (String) fruitCombo1.getValue(); // valeur de la géometrie --> localisation, symétrie etc...
                    System.out.println(value);
                    String box = case0.getText();
                    String parts[] = box.split("/");
                    System.out.println(parts[0]);
                    System.out.println(parts[1]);
                    System.out.println(parts[2]);

                    parts[0]= parts[0].substring(parts[0].lastIndexOf("Ø") + 1);
                    Double tolerr=Double.parseDouble(parts[0]);

                    System.out.println(parts[0]);

                    if(value.equals("Localisation")){

                        PreparedStatement st;
                        ResultSet result;

                        //DB things

                        Connection conn = null;
                        conn = db.ConnectDB();

                        st = conn.prepareStatement("SELECT * FROM Geometrie WHERE designation=? AND diametre=?");

                        st.setString(1, value);
                        st.setDouble(2, tolerr);

                        result = st.executeQuery();

                        Double tole = result.getDouble("tole");

                        System.out.println(tole);

                        //TOLERANCE DE LA BASE

                        BigDecimal tole11= BigDecimal.valueOf(tole);

                        //Valeur du diametre
                        Double cote = Double.parseDouble(parts[1]);
                        BigDecimal cote1 = BigDecimal.valueOf(cote);

                        //Valeur du textfield
                        Double j = Double.parseDouble(((TextField) node).getText());
                        BigDecimal jj = BigDecimal.valueOf(j);

                        if(jj.compareTo(cote1.add(tole11)) == 1 || jj.compareTo(cote1.subtract(tole11)) == -1 ){
                            (node).setStyle("-fx-background-color: red;");
                            case0.setStyle("-fx-background-color: whitesmoke;");
                        }
                        else {
                            (node).setStyle("-fx-background-color: whitesmoke;");
                        }

                        break;
                    }
                    else {
                        System.out.println("ICI ON NE FAIT RIEN --> Tolerances geometriques");
                        break;
                    }
                }

                //gère le cas ou on tape un contenu différent dans les textfields filles par rapport au textfield mère --> fonction de tolérance (différence avec tole et BDD Tolérateur)

                if (node instanceof TextField && !((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 1 && !((TextField) node).getText().equals(case0.getText()) && ( case0.getText().contains("+") || case0.getText().contains("-")
                        || case0.getText().contains("±") ) ){

                    //usefull stuff
                    String strr = case0.getText();
                    int index = strr.indexOf("+");
                    int countplus = 0;
                    while(index != -1){
                        countplus++;
                        strr = strr.substring(index + 1);
                        index = strr.indexOf("+");
                    }
                    System.out.println(countplus);

                    String strrr = case0.getText();
                    int indexx = strrr.indexOf("-");
                    int countmoins = 0;
                    while(indexx != -1){
                        countmoins++;
                        strrr = strrr.substring(indexx + 1);
                        indexx = strrr.indexOf("-");
                    }
                    System.out.println(countmoins);


                    // LE CAS : PLUS OU MOINS AVEC MEME TOLERANCE
                    if(strr.contains("±"))
                    {
                        String str = case0.getText(); //texte case mère
                        String toleranceString = str.substring(str.indexOf("±")+1); //la tolérance aprés le "±"
                        String tailleMere = str.split("±")[0]; //la taille réelle de la case mère ex: 15.1±0.1 --> ça prend 15.1
                        //System.out.println(str);
                        //System.out.println(toleranceString);
                        //System.out.println(tailleMere);

                        BigDecimal i = BigDecimal.valueOf(Double.parseDouble(tailleMere));
                        BigDecimal j = BigDecimal.valueOf(Double.parseDouble(((TextField) node).getText())); //texte des cases filles
                        BigDecimal toleranceDouble = BigDecimal.valueOf(Double.parseDouble(toleranceString)); // --> tolérance
                        BigDecimal k = i.subtract(j);

                        System.out.println(i + ", " + j + " = " + k);

                        //if(!((TextField) node).getText().equals(case0.getText())) {
                        if(k.abs().compareTo(toleranceDouble) == 1){
                            //la fonction de correspondance ira ici
                            (node).setStyle("-fx-background-color: red;");
                            case0.setStyle("-fx-background-color: whitesmoke;");
                        }
                        else {
                            (node).setStyle("-fx-background-color: whitesmoke;");
                        }
                    }

                    //LE CAS : 0 ET PLUS

                    if (countplus == 2){

                        String str = case0.getText(); //texte case mère
                        String toleranceString1 = str.substring(str.lastIndexOf("+") + 1); //la tolérance aprés le dernier "+"
                        String toleranceString2 = str.substring(str.indexOf("+") + 1); toleranceString2 = toleranceString2.substring(0, toleranceString2.indexOf("+"));//la tolérance du milieu entre les deux "+"
                        String tailleMere = str.split("\\+")[0]; //la taille réelle de la case mère ex: 15.1±0.1 --> ça prend 15.1
                        //System.out.println(str);
                        //System.out.println(toleranceString1);
                        //System.out.println(toleranceString2);
                        //System.out.println(tailleMere);

                        BigDecimal i = BigDecimal.valueOf(Double.parseDouble(tailleMere));
                        BigDecimal j = BigDecimal.valueOf(Double.parseDouble(((TextField) node).getText())); //texte des cases filles
                        BigDecimal toleranceDouble1 = BigDecimal.valueOf(Double.parseDouble(toleranceString1)); // --> tolérance1
                        BigDecimal toleranceDouble2 = BigDecimal.valueOf(Double.parseDouble(toleranceString2)); // --> tolérance2
                        BigDecimal k = j.subtract(i);

                        System.out.println(i + ", " + j + " = " + k);

                        if(j.compareTo(i.add(toleranceDouble2)) == -1 || j.compareTo(i.add(toleranceDouble1)) == 1) { // si j < a i + tolerance1 ou si j > i+ tolerance2 --> display red
                            //la fonction de correspondance ira ici
                            (node).setStyle("-fx-background-color: red;");
                            case0.setStyle("-fx-background-color: whitesmoke;");
                        }
                        else {
                            (node).setStyle("-fx-background-color: whitesmoke;");
                        }
                    }

                    if (countmoins == 2){

                        String str = case0.getText(); //texte case mère
                        String toleranceString1 = str.substring(str.lastIndexOf("-") + 1); //la tolérance aprés le dernier "+"
                        String toleranceString2 = str.substring(str.indexOf("-") + 1); toleranceString2 = toleranceString2.substring(0, toleranceString2.indexOf("-"));//la tolérance du milieu entre les deux "+"
                        String tailleMere = str.split("-")[0]; //la taille réelle de la case mère ex: 15.1±0.1 --> ça prend 15.1
                        //System.out.println(str);
                        //System.out.println(toleranceString1);
                        //System.out.println(toleranceString2);
                        //System.out.println(tailleMere);

                        BigDecimal i = BigDecimal.valueOf(Double.parseDouble(tailleMere));
                        BigDecimal j = BigDecimal.valueOf(Double.parseDouble(((TextField) node).getText())); // texte des cases filles
                        BigDecimal toleranceDouble1 = BigDecimal.valueOf(Double.parseDouble(toleranceString1)); // --> tolérance1
                        BigDecimal toleranceDouble2 = BigDecimal.valueOf(Double.parseDouble(toleranceString2)); // --> tolérance2
                        BigDecimal k = j.subtract(i);

                        System.out.println(i + ", " + j + " = " + k);

                        if(j.compareTo(i.subtract(toleranceDouble2)) == 1 || j.compareTo(i.subtract(toleranceDouble1)) == -1) { // si j < a i + tolerance1 ou si j > i+ tolerance2 --> display red
                            //la fonction de correspondance ira ici
                            (node).setStyle("-fx-background-color: red;");
                            case0.setStyle("-fx-background-color: whitesmoke;");
                        }
                        else {
                            (node).setStyle("-fx-background-color: whitesmoke;");
                        }
                    }


                    //LE CAS : PLUS ET MOINS AVEC TOLERANCE DIFFERENTE

                    if (countplus == 1 && countmoins== 1){

                        String str = case0.getText(); //texte case mère
                        String toleranceString1 = str.substring(str.lastIndexOf("-") + 1); //la tolérance aprés le dernier "+"
                        String toleranceString2 = str.substring(str.indexOf("+") + 1); toleranceString2 = toleranceString2.substring(0, toleranceString2.indexOf("-"));//la tolérance du milieu entre les deux "+" et "-"
                        String tailleMere = str.split("\\+")[0]; //la taille réelle de la case mère ex: 15.1±0.1 --> ça prend 15.1
                        System.out.println(str);
                        System.out.println(toleranceString1);
                        System.out.println(toleranceString2);
                        System.out.println(tailleMere);

                        BigDecimal i = BigDecimal.valueOf(Double.parseDouble(tailleMere));
                        BigDecimal j = BigDecimal.valueOf(Double.parseDouble(((TextField) node).getText())); //texte des cases filles
                        BigDecimal toleranceDouble1 = BigDecimal.valueOf(Double.parseDouble(toleranceString1)); // --> tolérance1
                        BigDecimal toleranceDouble2 = BigDecimal.valueOf(Double.parseDouble(toleranceString2)); // --> tolérance2
                        BigDecimal k = j.subtract(i);

                        System.out.println(i + ", " + j + " = " + k);

                        //if(!((TextField) node).getText().equals(case0.getText())) {
                        if(j.compareTo(i.add(toleranceDouble2)) == 1 || j.compareTo(i.subtract(toleranceDouble1)) == -1){
                            //la fonction de correspondance ira ici
                            (node).setStyle("-fx-background-color: red;");
                            case0.setStyle("-fx-background-color: whitesmoke;");
                        }
                        else {
                            (node).setStyle("-fx-background-color: whitesmoke;");
                        }
                    }
                    break;
                }

                //TOLERATOR GESTION
                if (node instanceof TextField && !((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 1 && !((TextField) node).getText().equals(case0.getText())) {
                    System.out.println("LETTRESSSSSSS");

                    String str = case0.getText();
                    String string = str.split("[a-zA-Z]", 2)[0]; // ex : 17g6 --> prend 17
                    String sttt = str.replaceAll("[^A-Za-z]", ""); //prend le g

                    //split en fonction du nombre de caractère
                    if (sttt.matches("[a-zA-Z]{2}")) {
                        System.out.println("TOLERANCE COMPOSE DE DEUX CARACTERES");
                        String test = str.split("[A-Za-z]")[2]; //prend le 6
                        sttt = sttt + test; //js1
                    } else {
                        System.out.println("TOLERANCE COMPOSE DE UN SEUL CARACTERE");
                        String test = str.split("[A-Za-z]")[1]; //prend le 6
                        sttt = sttt + test; //g6
                    }


                    System.out.println(sttt);

                    Double interval[] = {3.0, 6.0, 10.0, 18.0, 30.0, 40.0, 50.0, 65.0, 80.0, 100.0, 120.0, 140.0, 160.0, 180.0, 200.0, 225.0, 250.0, 280.0, 315.0, 355.0, 400.0, 450.0, 500.0};

                    Double myNumber = Double.parseDouble(string);

                    Double idx = 0.0;
                    int c = 0;
                    for (c = 0; c < interval.length; c++) {

                        if (myNumber > 0.0 && myNumber <= 3.0) {
                            idx = 3.0;
                        } else if (myNumber == interval[c]) {
                            idx = interval[c];
                        } else if (myNumber <= interval[c]) {
                            idx = interval[c];
                        }

                        if (idx != 0) {
                            break;
                        }
                    }

                    System.out.println("interval --> " + idx);

                    PreparedStatement st;
                    ResultSet result;

                    // DECISIONS ENTRE ARBRE ET ALESAGE

                    if (sttt.matches(".*[A-Z].*")) {
                        System.out.println("ALESAGE");

                        //DB things

                        Connection conn = null;
                        conn = db.ConnectDB();

                        st = conn.prepareStatement("SELECT * FROM Alesage WHERE designation=? AND interval=?");

                        st.setString(1, sttt);
                        st.setDouble(2, idx);

                        result = st.executeQuery();

                    } else {
                        System.out.println("ARBRES");

                        //DB things

                        Connection conn = null;
                        conn = db.ConnectDB();

                        st = conn.prepareStatement("SELECT * FROM Arbres WHERE designation=? AND interval=?");

                        st.setString(1, sttt);
                        st.setDouble(2, idx);

                        result = st.executeQuery();
                    }

                    if (!result.next()) {
                        //ResultSet is empty
                        idx = interval[c + 1];
                        st.setString(1, sttt);
                        st.setDouble(2, idx);
                        System.out.println("Interval non trouvé pour cette ligne la, on saute au suivant : --> " + idx);
                        result = st.executeQuery();

                        if (!result.next()) {
                            idx = interval[c + 2];
                            st.setString(1, sttt);
                            st.setDouble(2, idx);
                            System.out.println("Interval non trouvé pour cette ligne la, on saute ENCORE au suivant : --> " + idx);
                            result = st.executeQuery();
                        }
                    }

                    String toleinf = result.getString("toleinf");
                    String tolesup = result.getString("tolesup");

                    Double toleinf1 = Double.parseDouble(toleinf);
                    BigDecimal toleinf11 = BigDecimal.valueOf(toleinf1);
                    Double tolesup1 = Double.parseDouble(tolesup);
                    BigDecimal tolesup11 = BigDecimal.valueOf(tolesup1);

                    BigDecimal mille = new BigDecimal(1000);
                    System.out.println("toleinf from BD --> " + toleinf1);
                    System.out.println("tolesup from BD --> " + tolesup1);
                    System.out.println("toleinf en Micron --> " + toleinf11.divide(mille));
                    System.out.println("tolesup en Micron --> " + tolesup11.divide(mille));

                    Double j = Double.parseDouble(((TextField) node).getText());
                    BigDecimal jj = BigDecimal.valueOf(j);

                    BigDecimal myNumber1 = BigDecimal.valueOf(myNumber);

                    BigDecimal maToleranceInf = myNumber1.add(toleinf11.divide(mille));
                    BigDecimal maToleranceSup = myNumber1.add(tolesup11.divide(mille));
                    System.out.println("Range : " + maToleranceSup + ".." + maToleranceInf);

                    if (!toleinf11.abs().equals(tolesup11.abs())) {
                        //if(jj.compareTo(maToleranceSup) == 1 || jj.compareTo(maToleranceInf) == -1){
                        if ((jj.compareTo(maToleranceSup) == 1 && jj.compareTo(maToleranceInf) == -1) || jj.compareTo(maToleranceSup) == 0 || jj.compareTo(maToleranceInf) == 0) {
                            //la fonction de correspondance ira ici
                            (node).setStyle("-fx-background-color: whitesmoke;");
                        } else {
                            (node).setStyle("-fx-background-color: red;");
                            case0.setStyle("-fx-background-color: whitesmoke;");
                        }
                    } else {
                        if ((jj.compareTo(maToleranceSup) == -1 && jj.compareTo(maToleranceInf) == 1) || jj.compareTo(maToleranceSup) == 0 || jj.compareTo(maToleranceInf) == 0) {
                            (node).setStyle("-fx-background-color: whitesmoke;");
                        } else {
                            (node).setStyle("-fx-background-color: red;");
                            case0.setStyle("-fx-background-color: whitesmoke;");
                        }
                    }
                }
            }
        }


        if (!case1.getText().isEmpty()) {
            for (Node node : grid2.getChildren()) {
                //gère le cas ou on efface le contenu des textfields filles
                if (node instanceof TextField && ((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 2){
                    (node).setStyle("-fx-background-color: whitesmoke;");
                }
                //gère le cas ou on tape un contenu différent dans les textfields filles par rapport au textfield mère
                if (node instanceof TextField && !((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 2 && !((TextField) node).getText().equals(case1.getText())) {
                    //la fonction de correspondance ira ici
                    (node).setStyle("-fx-background-color: red;");
                    case1.setStyle("-fx-background-color: whitesmoke;");
                }
                //gère le cas ou on tape un contenu identique dans les textfields filles par rapport au textfield mère
                if (node instanceof TextField && !((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 2 && ((TextField) node).getText().equals(case1.getText())) {
                    //la fonction de correspondance ira ici
                    (node).setStyle("-fx-background-color: whitesmoke;");
                    case1.setStyle("-fx-background-color: whitesmoke;");
                }
            }
        }
    }

    @FXML
    public void TestCouleur2(){
        if (case0.getText().isEmpty()){
            for (Node node : grid2.getChildren()) {
                //System.out.println("Id: " + node.getId());
                if (node instanceof TextField && !((TextField) node).getText().isEmpty() && grid2.getRowIndex(node) == 1) {
                    //la fonction de correspondance ira ici
                    (node).setStyle("-fx-background-color: whitesmoke;");
                }
            }
        }
    }

    @FXML
    Label dataLabel;

    @FXML
    GridPane grid0, grid1, grid2, grid3;

    @FXML
    ComboBox fruitCombo1, fruitCombo2;

    @FXML
    protected void initialize(){

        //Set la date actuelle
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //get current date time with Date()
        Date date = new Date();

        dataLabel.setText("Date :" + "  " + dateFormat.format(date));

        //Met tout les textfield existant du gridpane principal en white

        for (Node node : grid2.getChildren()) {
            //System.out.println("Id: " + node.getId());
            if (node instanceof TextField) {
                ((TextField) node).setStyle("-fx-background-color: whitesmoke;");
            }
        }



        //ObservableList<String> list = FXCollections.observableArrayList("./Tolerances/white", "./Tolerances/Circularite", "./Tolerances/Localisation", "./Tolerances/Coaxialit", "./Tolerances/Cylindricite", "./Tolerances/Inclinaison", "./Tolerances/Parallelisme", "./Tolerances/Perpendicularite", "./Tolerances/Planeite", "./Tolerances/Rectitude", "./Tolerances/Symetrie", "./Tolerances/Forme_ligne", "./Tolerances/Forme_surface");
        //Combo Box items

        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll("white", "Localisation", "Circularite", "Coaxialit", "Cylindricite", "Inclinaison", "Parallelisme", "Perpendicularite", "Planeite", "Rectitude", "Symetrie", "Forme_ligne", "Forme_surface");
        fruitCombo1.setItems(list);
        fruitCombo1.setCellFactory(c -> new MyListCell());
        fruitCombo1.setButtonCell(new MyListCell());


    }

    //Attention, cela marche mais fire une erreur en conflit avec l'autre Listener des textFields de la grille
    @FXML
    public void resetComboBox(){
        if(fruitCombo1.getValue() == "white"){
            fruitCombo1.getSelectionModel().clearSelection();
        }
    }
}

