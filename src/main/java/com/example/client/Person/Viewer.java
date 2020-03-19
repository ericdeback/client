package com.example.client.Person;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.Component;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.*;

@Service
public class Viewer {
    private static Insets insets = new Insets(5, 5, 5, 5);
    private JFrame frame;
    private JTextField url_textfield;
    private JTextArea responsebody_textarea;
    private JTextArea send_textarea;
    private JButton send_button;
    private JButton save_button;
    private JButton open_button;
    private JButton test_button;
    private JLabel responsecode_label;
    private JLabel responsetime_label;
    private JLabel responsesize_label;
    private JComboBox httpCommandsList;
    private JScrollPane responsescroll;
    private JScrollPane sendscroll;
    private JTable responseheader_table;
    private JTextArea testArea;
    private PersonController controllerService;
    private String response_code_static = "Response code: ";
    private String response_time_static = "Time: ";
    private String response_size_static = "Size: ";
    private Map<String, HttpMethod> HttpMethodMap;
    private int width = 1024;
    private int height = 800;
    private final String defaultFolder = "c:\\data\\queries";
    private DefaultTableModel responseHeadersModel;
    private DefaultTableModel cookieModel;

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    public Viewer(PersonController controllerService){
        this.controllerService = controllerService;
    }

    @Bean
    public void createFrame() {
        frame = new JFrame("Spring Boot Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // TODO
        JPanel pane1 = new JPanel(new BorderLayout());

        // create the http commands are a map from string to httpmethod enums
        HttpMethodMap = new HashMap<>();
        String[] commands = {"Get", "Post", "Put", "Delete", "Patch"};
        HttpMethodMap.put(commands[0], HttpMethod.GET);
        HttpMethodMap.put(commands[1], HttpMethod.POST);
        HttpMethodMap.put(commands[2], HttpMethod.PUT);
        HttpMethodMap.put(commands[3], HttpMethod.DELETE);
        HttpMethodMap.put(commands[4], HttpMethod.PATCH);
        httpCommandsList = new JComboBox(commands);
        httpCommandsList.setLightWeightPopupEnabled(false);
        url_textfield = new JTextField("http://localhost:8081/api/v1/person/19efecbf-1243-4656-a461-58a61f9e5dd5");

        send_button = new JButton("Send");
        save_button = new JButton("Save");
        open_button = new JButton("Open");
        test_button = new JButton("Test");

        responsecode_label = new JLabel(response_code_static);
        responsetime_label = new JLabel(response_time_static);
        responsesize_label = new JLabel(response_size_static);

        // send area
        JTabbedPane tabpaneSend = new JTabbedPane();

        send_textarea = new JTextArea("");
        Font font = new Font("courier", Font.PLAIN, 12);
        send_textarea.setFont(font);
        sendscroll = new JScrollPane (send_textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        testArea = new JTextArea();

        tabpaneSend.addTab("Test", testArea);
        tabpaneSend.addTab("Body", sendscroll);
        tabpaneSend.addTab("Params", null);
        tabpaneSend.addTab("Headers", null);

        // response area
        JTabbedPane tabpaneResponse = new JTabbedPane();

        responsebody_textarea = new JTextArea("");
        font = new Font("courier", Font.PLAIN, 12);
        responsebody_textarea.setFont(font);
        responsescroll = new JScrollPane (responsebody_textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JTable responseheader_table = new JTable(new DefaultTableModel(new Object[]{"KEY", "VALUE"}, 0));
        responseHeadersModel = (DefaultTableModel) responseheader_table.getModel();
        JScrollPane responseHeadersScrollPane = new JScrollPane(responseheader_table);

        JTable cookie_table = new JTable(new DefaultTableModel(new Object[]{"Name", "Value", "Domain", "Path", "Expires", "HttpOnly", "Secure"}, 0));
        cookieModel = (DefaultTableModel) cookie_table.getModel();
        JScrollPane cookieScrollPane = new JScrollPane(cookie_table);

        tabpaneResponse.addTab("Body", responsescroll);
        tabpaneResponse.addTab("Headers", responseHeadersScrollPane);
        tabpaneResponse.addTab("Cookies", cookieScrollPane);

        addComponent(frame, httpCommandsList,           0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, url_textfield,              1, 0, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, send_button,                3, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        //addComponent(frame, new Label("TBA"),      0, 1, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, open_button,                0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, save_button,                1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, test_button,                2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, tabpaneSend,                0, 2, 4, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.4f );
        addComponent(frame, responsecode_label,         0, 3, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, responsetime_label,         2, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, responsesize_label,         3, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05f );
        addComponent(frame, tabpaneResponse,            0, 4, 4, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.4f );

        frame.setPreferredSize(new Dimension (width, height));
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }

    Object[] getCookieValues(String cookie){

        String[]keys ={"Domain", "Path", "Expires", "HttpOnly", "Secure"};

        List<String> list = new ArrayList<>();
        int m1, m2;

        // cookie name
        m2 = cookie.indexOf("=", 0);
        list.add(cookie.substring(0, m2));

        m1 = cookie.indexOf("=", 0)+1;
        m2 = cookie.indexOf(";", 0);
        list.add(cookie.substring(m1, m2));


        for (String tkey: keys) {

            int n = cookie.indexOf(tkey);

            if(!tkey.equals("HttpOnly") && !tkey.equals("Secure")) {
                m1 = cookie.indexOf("=", n)+1;
                m2 = cookie.indexOf(";", n);
                if (m2==-1) m2 = cookie.length()-1;
                list.add(cookie.substring(m1, m2));
            }
            if(tkey.equals("HttpOnly")) {
                if (cookie.indexOf(tkey)>-1) {
                    list.add("true");
                } else {
                    list.add("false");
                }
            }
            if(tkey.equals("Secure")) {
                if (cookie.indexOf(tkey)>-1) {
                    list.add("true");
                } else {
                    list.add("false");
                }
            }
        }
        Object[] obj = new Object[list.size()];
        return list.toArray(obj);
    }


    @Bean
    public void processFrameEvents() {

        test_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                log.info("Processing test_button action with GET command");

                // these values have been collected from the GUI
                String cookie = "UID=debacke; Password=password";
                ResponseEntity<String> entity = controllerService.test("http://localhost:8081/login", cookie);

                responsebody_textarea.setText(entity.getBody());

                // build header key/value pairs in the responseHeaders table (model)
                Map<String, String> headerMap = entity.getHeaders().toSingleValueMap();
                for (String key : headerMap.keySet()) {
                    responseHeadersModel.addRow(new Object[]{key,headerMap.get(key)});

                    if(key.equals("Set-Cookie")) {
                        cookieModel.addRow(getCookieValues(headerMap.get(key)));
                    }
                }

                //
                // HERE WE KNOW OUR SESSION ID!!!. This should be used in all requests from now on.
                //

            }
        });

        send_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                HttpMethod httpmethod =  HttpMethodMap.get(httpCommandsList.getSelectedItem());
                final String spacing = "   ";

                Person person = null;
                switch ( httpmethod ) {
                    case GET :

                        log.info("Processing send_button action with GET command");

                        ResponseEntity<List<Person>> entity = null;
                        try {
                            long start = System.nanoTime();
                            entity = controllerService.getPersons(url_textfield.getText());
                            long delta = System.nanoTime()-start;

                            // Convert body to JSON
                            List<Person> personList = entity.getBody();

                            HttpStatus http_response_code = entity.getStatusCode();

                            responsebody_textarea.setText(Person.formatToJSON(personList));
                            responsebody_textarea.setCaretPosition(0);

                            responsecode_label.setText(response_code_static + http_response_code.toString());
                            responsetime_label.setText(response_time_static + (delta / 1000000l) + "ms");

                            // build header key/value pairs in the responseHeaders table (model)
                            Map<String, String> headerMap = entity.getHeaders().toSingleValueMap();

                            // clear the cookies & header tables
                            cookieModel.setRowCount(0);
                            responseHeadersModel.setRowCount(0);

                            // build the header and cookie table
                            for (String key : headerMap.keySet()) {
                                responseHeadersModel.addRow(new Object[]{key,headerMap.get(key)});
                                if (key.equals("Set-Cookie")) {
                                    cookieModel.addRow(getCookieValues(headerMap.get(key)));
                                }
                            }

                        } catch (HttpClientErrorException ex) {
                            responsecode_label.setText(ex.getStatusText());
                            responsebody_textarea.setText(ex.toString());
                        }
                        break;
                    case POST:
                        try {

                            log.info("Processing send_button action with POST command");

                            // use Jackson JSON parser to create a new Person object
                            person = new ObjectMapper().readValue(send_textarea.getText(), Person.class);

                            long start = System.nanoTime();
                            ResponseEntity<String> postentity = controllerService.postPersons(url_textfield.getText(), person);
                            long delta = System.nanoTime()-start;

                            HttpStatus http_response_code = postentity.getStatusCode();

                            if (postentity.hasBody()) {
                                responsebody_textarea.setText("");
                            }
                            responsecode_label.setText(response_code_static + http_response_code.toString());
                            responsetime_label.setText(response_time_static + (delta / 1000000l) + "ms");

                        } catch (JsonProcessingException ex) {
                            System.out.println("Not valid JSON"); // TODO need better warning for user
                        } catch (HttpClientErrorException ex) {
                            responsecode_label.setText(ex.getStatusText());
                            responsebody_textarea.setText(ex.toString());
                        }

                        break;
                    case PUT:
                        try {

                            log.info("Processing send_button action with PUT command");

                            // use Jackson JSON parser to create a new Person object
                            person = new ObjectMapper().readValue(send_textarea.getText(), Person.class);

                            long start = System.nanoTime();
                            ResponseEntity<String> postentity = controllerService.putPersons(url_textfield.getText(), person);
                            long delta = System.nanoTime()-start;

                            HttpStatus http_response_code = postentity.getStatusCode();

                            responsebody_textarea.setText("");
                            responsecode_label.setText(response_code_static + http_response_code.toString());
                            responsetime_label.setText(response_time_static + (delta / 1000000l) + "ms");

                        } catch (JsonProcessingException ex) {
                            //System.out.println("Not valid JSON"); // TODO need better warning for user
                            log.info("Exception: Not valid JSON");
                        } catch (HttpClientErrorException ex) {
                            responsecode_label.setText(ex.getStatusText());
                            responsebody_textarea.setText(ex.toString());

                        } catch (Exception ex) {
                            System.out.println(ex.getStackTrace());
                        }
                        break;
                    case DELETE:

                        log.info("Processing send_button action with DELETE command: TBA");
                        break;
                    case PATCH:

                        log.info("Processing send_button action with PATCH command: TBA");
                        break;
                }

            }
        });

        save_button.addActionListener( e-> saveQuery());
        open_button.addActionListener( e-> openQuery());
    }

    private void saveQuery() {

        try {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setCurrentDirectory(new File("c:\\data\\queries"));
            fc.setDialogTitle("Save Query");

            int returnVal = fc.showSaveDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));

                bw.write("{\n");
                bw.write("\"url\": \"" + url_textfield.getText() + "\"\n");
                bw.write("\"command\": \"" + httpCommandsList.getSelectedItem().toString()+ "\"\n");

                if(httpCommandsList.getSelectedItem().toString().equals("Post") || httpCommandsList.getSelectedItem().toString().equals("Put") ) {
                    bw.write("\"body\": \"" + responsebody_textarea.getText()+ "\"\n");
                }
                bw.write("}");
                bw.close();

            } else {
                System.out.println("Save command cancelled by user.");
            }
        } catch (IOException ex) {
        }
    }

    private void openQuery() {

        try {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setCurrentDirectory(new File("c:\\data\\queries"));
            fc.setDialogTitle("Open Query");

            int returnVal = fc.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                String thisLine = null;

                // quick amd dirty for now
                br.readLine(); // read past {

                String urlstr =    br.readLine().split(": ")[1].trim();
                urlstr = urlstr.substring(1, urlstr.length()-1);

                String commandstr =br.readLine().split(": ")[1].trim();
                commandstr = commandstr.substring(1, commandstr.length()-1);

                String bodystr="";
                if (commandstr.equals("Post") || commandstr.equals("Put")) { // maybe not necessary? can be null?
                    bodystr =br.readLine().trim();
                    bodystr = bodystr.substring(1, bodystr.length()-1);
                }

                url_textfield.setText(urlstr);
                httpCommandsList.setSelectedItem(commandstr);
                send_textarea.setText(bodystr);

            } else {
                System.out.println("Open command cancelled by user.");
            }
        } catch (IOException ex) {
        }
    }

    private static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, float weighty) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, weighty, anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }

}
