package org.ns.log;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author stupak
 */
public class GuiLoggingDialog extends JDialog {
    
    public static final Icon IC_QUESTION;
    public static final Icon IC_INFORMATION;
    public static final Icon IC_WARNING;
    public static final Icon IC_ERROR;

    static {
        UIDefaults res = UIManager.getLookAndFeelDefaults();
        IC_QUESTION = res.getIcon("OptionPane.questionIcon");
        IC_INFORMATION = res.getIcon("OptionPane.informationIcon");
        IC_WARNING = res.getIcon("OptionPane.warningIcon");
        IC_ERROR = res.getIcon("OptionPane.errorIcon");
    }
    
    private static final String PREFIX                            = "org.nikolaas.gma.util.log.GuiLoggingDialog.";
    
    private static final String TITLE_TEXT                        = locale("title.text");
    private static final String TO_FIRST_BUTTON_TEXT              = locale("toFirstButton.text");
    private static final String TO_PREV_BUTTON_TEXT               = locale("toPrevButton.text");
    private static final String TO_NEXT_BUTTON_TEXT               = locale("toNextButton.text");
    private static final String TO_LAST_BUTTON_TEXT               = locale("toLastButton.text");
    private static final String CANCEL_BUTTON_TEXT                = locale("cancelButton.text");
    private static final String MESSAGE_COUNT_LABEL_TEXT_TEMPLATE = locale("messageCountLabel.text.template");
    private static final String NO_MESSAGE_LABEL_TEXT             = locale("noMessageLabel.text");
    
    private static final Map<String, String> bundle = new HashMap<>();
    static {
        bundle.put(PREFIX + TITLE_TEXT,                         "Сообщения лога");
        bundle.put(PREFIX + TO_FIRST_BUTTON_TEXT,               "<<");
        bundle.put(PREFIX + TO_PREV_BUTTON_TEXT,                "<");
        bundle.put(PREFIX + TO_NEXT_BUTTON_TEXT,                ">");
        bundle.put(PREFIX + TO_LAST_BUTTON_TEXT,                ">>");
        bundle.put(PREFIX + CANCEL_BUTTON_TEXT,                 "Отмена");
        bundle.put(PREFIX + MESSAGE_COUNT_LABEL_TEXT_TEMPLATE,  "Сообщение {0} из {1}");
        bundle.put(PREFIX + NO_MESSAGE_LABEL_TEXT,              "Сообщения отсуствуют");
    }
    
    private static String locale(String key) {
        String fullKey = PREFIX + key;
        String localeString = null;
        try {
            localeString = bundle.get(fullKey);
        } catch (Exception ex) {}
        if ( localeString == null || localeString.equals("") ) {
            localeString = key;
        }
        return localeString;
    }
    
    private JPanel messageContent;
    private JButton toFirstButton;
    private JButton toPrevButton;
    private JButton toNextButton;
    private JButton toLastButton;
    private JButton cancelButton;
    private JLabel messageCountLabel;
    
    private final Map<Level, GuiMessage> guiMessageCache;
    private final List<LogMessage> messages;
    private boolean showOnNewMessage;
    
    private int currentMessageIndex;

    public GuiLoggingDialog(Window owner) {
        super(owner);
        setTitle(locale(TITLE_TEXT));
        setModal(false);
        setModalityType(ModalityType.MODELESS);
        
        this.messages = new ArrayList<>();
        this.guiMessageCache = new HashMap<>();
        this.currentMessageIndex = -1;
        
        initComponents();
        layoutComponents();
    }
    
    private void initComponents() {
        messageContent = new JPanel();
        messageContent.setLayout(new BorderLayout());
        
        toFirstButton = new JButton(TO_FIRST_BUTTON_TEXT);
        toFirstButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toFirstMessage();
            }
        });
        
        toPrevButton = new JButton(TO_PREV_BUTTON_TEXT);
        toPrevButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toPrevMessage();
            }
        });
        
        toNextButton = new JButton(TO_NEXT_BUTTON_TEXT);
        toNextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toNextMessage();
            }
        });
        
        toLastButton = new JButton(TO_LAST_BUTTON_TEXT);
        toLastButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toLastMessage();
            }
        });
        
        cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GuiLoggingDialog.this.setVisible(false);
            }
        });
        
        messageCountLabel = new JLabel();
        updateMessageCountLabel();
    }
    
    private void layoutComponents() {
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(messageContent, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Integer.MAX_VALUE)    
                        .addComponent(messageCountLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Integer.MAX_VALUE)
                        .addComponent(toFirstButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toPrevButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toNextButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toLastButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Integer.MAX_VALUE)    
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                )
                .addContainerGap()
        );
        
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messageContent, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageCountLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(toFirstButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(toPrevButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(toNextButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(toLastButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
        );
    }
    
    private void toFirstMessage() {
        toMessage(0);
    }
    
    private void toPrevMessage() {
        toMessage(currentMessageIndex - 1);
    }
    
    private void toNextMessage() {
        toMessage(currentMessageIndex + 1);
    }
    
    private void toLastMessage() {
        toMessage(messages.size() - 1);
    }
    
    private void toMessage(int index) {
        showMessage(index);
        currentMessageIndex = index;
        updateGui();
    }
    
    public boolean isShowOnNewMessage() {
        return showOnNewMessage;
    }

    public void setShowOnNewMessage(boolean showOnNewMessage) {
        this.showOnNewMessage = showOnNewMessage;
    }
    
    public void update(List<LogMessage> newMessages) {
        this.messages.addAll(newMessages);
        updateGui();
    }
    
    private void updateGui() {
        // обновляем активность кнопок навигации
        toFirstButton.setEnabled(currentMessageIndex > 0);
        toPrevButton.setEnabled(currentMessageIndex > 0);
        toNextButton.setEnabled(currentMessageIndex < messages.size() - 1);
        toLastButton.setEnabled(currentMessageIndex < messages.size() - 1);
        
        // обновляем метку общего количества сообщений
        updateMessageCountLabel();
    }
    
    private void updateMessageCountLabel() {
        if ( messages.isEmpty() ) {
            messageCountLabel.setText(NO_MESSAGE_LABEL_TEXT);
        } else {
            messageCountLabel.setText(MessageFormat.format(MESSAGE_COUNT_LABEL_TEXT_TEMPLATE, currentMessageIndex + 1, messages.size()));
        }
    }
    
    
    private void showMessage(int index) {
        if ( index == currentMessageIndex ) {
            return;
        }
        LogMessage message = messages.get(index);
        GuiMessage guiMessage = getGuiMessage(message);
        messageContent.removeAll();
        messageContent.add(guiMessage, BorderLayout.CENTER);
    }
    
    private GuiMessage getGuiMessage(LogMessage message) {
        GuiMessage guiMessage = guiMessageCache.get(message.getLevel());
        if ( guiMessage == null ) {
            guiMessage = new GuiMessage();
            guiMessageCache.put(message.getLevel(), guiMessage);
        }
        guiMessage.setMessage(message);
        return guiMessage;
    }
    
    private static class GuiMessage extends JPanel {

        private JLabel iconComponent;
        private JScrollPane scroll;
        private JTextArea text;
        
        public GuiMessage() {
            super();
            initComponents();
            initLayout();
        }
        
        private void initComponents() {
            iconComponent = new JLabel();
            scroll = new JScrollPane();
            text = new JTextArea();
        }
        
        private void initLayout() {
            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            
            layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addComponent(iconComponent, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(scroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
            );
            
            layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(iconComponent, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(scroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
            );
            
            scroll.setViewportView(text);
        }
        
        public void setMessage(LogMessage message) {
            iconComponent.setIcon(getIconByLevel(message.getLevel()));
            text.setText(message.getMessage());
            this.invalidate();
            this.validate();
        }
        
        private Icon getIconByLevel(Level l){
            Icon res = IC_INFORMATION;
            if(l != null){
                if(Level.SEVERE.equals(l)){
                    res = IC_ERROR;
                } else if(Level.WARNING.equals(l)){
                    res = IC_WARNING;
                }
            }
            return res;
        }
    }
}
