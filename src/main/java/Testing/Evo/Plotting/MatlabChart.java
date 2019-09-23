package Testing.Evo.Plotting;


/**
 * CREDIT
 * https://stackoverflow.com/a/38933056
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;


public class MatlabChart {

    Font font;
    JFreeChart chart;
    LegendTitle legend;
    ArrayList<Color> colors;
    ArrayList<Stroke> strokes;
    XYSeriesCollection dataset;

    public MatlabChart() {
        font = JFreeChart.DEFAULT_TITLE_FONT;
        colors = new ArrayList<Color>();
        strokes = new ArrayList<Stroke>();
        dataset = new XYSeriesCollection();
    }

    public void plot(double[] x, double[] y, String spec, float lineWidth, String title) {
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++)
            series.add(x[i],y[i]);
        dataset.addSeries(series);
        FindColor(spec,lineWidth);
    }

    public void RenderPlot() {
        // Create chart
        JFreeChart chart = null;
        if (dataset != null && dataset.getSeriesCount() > 0)
            chart = ChartFactory.createXYLineChart(null,null,null,dataset,PlotOrientation.VERTICAL,true, false, false);
        else
            System.out.println(" [!] First create a chart and add data to it. The plot is empty now!");
        // Add customization options to chart
        XYPlot plot = chart.getXYPlot();
        for (int i = 0; i < colors.size(); i++) {
            plot.getRenderer().setSeriesPaint(i, colors.get(i));
            plot.getRenderer().setSeriesStroke(i, strokes.get(i));
        }
        ((NumberAxis)plot.getDomainAxis()).setAutoRangeIncludesZero(false);
        ((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
        plot.setBackgroundPaint(Color.WHITE);
        legend = chart.getLegend();
        chart.removeLegend();
        this.chart = chart;
    }

    public void CheckExists() {
        if (chart == null) {
            throw new IllegalArgumentException("First plot something in the chart before you modify it.");
        }
    }

    public void font(String name, int fontSize) {
        CheckExists();
        font = new Font(name, Font.PLAIN, fontSize);
        chart.getTitle().setFont(font);
        chart.getXYPlot().getDomainAxis().setLabelFont(font);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);
        legend.setItemFont(font);
    }

    public void title(String title) {
        CheckExists();
        chart.setTitle(title);
    }

    public void xlim(double l, double u) {
        CheckExists();
        chart.getXYPlot().getDomainAxis().setRange(l, u);
    }

    public void ylim(double l, double u) {
        CheckExists();
        chart.getXYPlot().getRangeAxis().setRange(l, u);
    }

    public void xlabel(String label) {
        CheckExists();
        chart.getXYPlot().getDomainAxis().setLabel(label);
    }

    public void ylabel(String label) {
        CheckExists();
        chart.getXYPlot().getRangeAxis().setLabel(label);
    }

    public void saveas(String fileName, int width, int height) {
        CheckExists();
        File file = new File(fileName);
        try {
            ChartUtilities.saveChartAsJPEG(file,this.chart,width,height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FindColor(String spec, float lineWidth) {
        float dash[] = {5.0f};
        float dot[] = {lineWidth};
        Color color = Color.RED;                    // Default color is red
        Stroke stroke = new BasicStroke(lineWidth); // Default stroke is line   
        if (spec.contains("-"))
            stroke = new BasicStroke(lineWidth);
        else if (spec.contains(":"))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        else if (spec.contains("."))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
        if (spec.contains("y"))
            color = Color.YELLOW;
        else if (spec.contains("m"))
            color = Color.MAGENTA;
        else if (spec.contains("c"))
            color = Color.CYAN;
        else if (spec.contains("r"))
            color = Color.RED;
        else if (spec.contains("g"))
            color = Color.GREEN;
        else if (spec.contains("b"))
            color = Color.BLUE;
        else if (spec.contains("k"))
            color = Color.BLACK;
        colors.add(color);
        strokes.add(stroke);
    }
}