import javax.swing.*;
import javax.swing.text.Document;

import java.awt.*;
import java.util.Map;

public class PlaceholderText extends JTextField {

    private String placeholder;

    private static Map<?, ?> hints = (Map<?, ?>) Toolkit.getDefaultToolkit()
            .getDesktopProperty("awt.font.desktophints");

    public static void main(String[] args) {
        final PlaceholderText pT = new PlaceholderText("");

    }

    public PlaceholderText() {
        super();
    }

    public PlaceholderText(final Document pDOc, final String pText, final int pColumns) {
        super(pDOc, pText, pColumns);
    }

    public PlaceholderText(final int pColumns) {
        super(pColumns);
    }

    public PlaceholderText(final String pString) {
        super(pString);
    }

    public PlaceholderText(final String pText, final int pColumns) {
        super(pText, pColumns);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    protected void paintComponent(final Graphics pG) {

        if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;

        g.setColor(new Color(200, 200, 255)); // Light blue background
        g.fillRect(0, 0, getWidth(), getHeight());

        super.paint(pG);

        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        if (hints != null) {
            g.setRenderingHints(hints);
        }
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics().getMaxAscent() + getInsets().top);

    }

    public void setPlaceholder(final String s) {
        placeholder = "s";
    }
}
