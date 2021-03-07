package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MyDesktop extends JPanel {

    private final MyTitleBar myTitleBar;
    private final MyContent myContent;

    public MyDesktop() {
//        setLayout(new BorderLayout());
        myTitleBar = new MyTitleBar();
        myTitleBar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });
        add(myTitleBar);

        myContent = new MyContent();
        myContent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                System.out.println(myContent.getBounds());
            }
        });
        add(myContent);


//        addComponentListener();
    }


    @Override
    protected void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);

        int id = e.getID();
        switch (id) {
            case ComponentEvent.COMPONENT_RESIZED:

                setComponentSize();
                break;
            case ComponentEvent.COMPONENT_MOVED:

                break;
            case ComponentEvent.COMPONENT_SHOWN:
                break;
            case ComponentEvent.COMPONENT_HIDDEN:
                break;
        }
    }

    private void setComponentSize() {
        if (myTitleBar != null) {
            myTitleBar.setBounds(0, 0, getWidth(), 38);
            myTitleBar.repaint(myTitleBar.getBounds());
            myContent.setBounds(0, myTitleBar.getHeight(), getWidth(), getHeight() - myTitleBar.getHeight());
            myContent.repaint(myContent.getBounds());
            repaint(getBounds());
        }
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
        if (propertyName.contains("size")) {
            int a = 0;
        }
    }

    @Override
    public Dimension getSize(Dimension rv) {
        return super.getSize(rv);
    }

    public void componetList(ComponentEvent event) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();
        try {
//            graphics2D.setColor(new Color(255, 255, 228));
//
//            graphics2D.fillRect(getX(), getY(), getWidth(), getHeight());
//            graphics2D.setColor(new Color(0, 0, 96));
//            graphics2D.drawString(String.format("我是主窗体 %s", getSize()), getWidth() / 2, getHeight() / 2);
            graphics2D.dispose();
        } catch (Exception exception) {
            throw exception;
        }
    }


    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);

    }

    @Override
    public Dimension getPreferredSize() {
        int a = 0;
        return super.getPreferredSize();

    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        super.setMaximumSize(maximumSize);
        int a = 0;
    }

    @Override
    public Dimension getMaximumSize() {
        int a = 0;
        return super.getMaximumSize();
    }
}
