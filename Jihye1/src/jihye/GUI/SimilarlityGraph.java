package jihye.GUI;


import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import javax.swing.*;

import jihye.PS.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.*;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

/**
 * A bar chart that uses a custom renderer to display different colors within a series.
 * No legend is displayed because there is only one series but the colors are not consistent.
 *
 */
public class SimilarlityGraph {
    
    public JPanel getChart(ResultData resultData, int correctNumber)
    {
    	//그래프를 그리기 위한 dataset 만듬.
    	final CategoryDataset dataset = createDataset(resultData);
    	//그래프 생성
        final JFreeChart chart = createChart(dataset,correctNumber);
        //그래프를 JPanel로 만듬.
        final ChartPanel chartPanel = new ChartPanel(chart);
        //중요함! JPanel 크기 설정
        chartPanel.setPreferredSize(new java.awt.Dimension(511,499));
        
        return chartPanel;    	
    }


    private CategoryDataset createDataset(ResultData resultData) {
    	  		
  		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset(); 
  		
  		
  		for(int i=0; i<resultData.choices.size();i++)
  		{
  			//일치하는 데이터가 없을시 일치도가 음수(-2)로 넘어오는데 이를 예외처리함.
  			if(resultData.similiarty.get(i) >= 0)
  				categoryDataset.addValue(resultData.similiarty.get(i), "", resultData.choices.get(i));
  			else
  				categoryDataset.addValue(0, "", resultData.choices.get(i));
  				
  		}
          
  		return categoryDataset;
    }

    private JFreeChart createChart(final CategoryDataset dataset, int correctNumber) {

        final JFreeChart chart = ChartFactory.createBarChart(
            "",       // chart title
            "",               // domain axis label
            "",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // the plot orientation
            false,                    // include legend
            true,
            false
        );

        chart.setBackgroundPaint(new Color(239,239,239));

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setTickLabelFont(new Font("Gulim", Font.PLAIN, 14));
        plot.setNoDataMessage("NO DATA!");
        
        //rendering하는 객체 생성.
        final CategoryItemRenderer renderer = new GraphRenderer(correctNumber);
        
        //renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
        renderer.setItemLabelsVisible(true);
        final ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        renderer.setPositiveItemLabelPosition(p);
        
        plot.setRenderer(renderer);

        // change the margin at the top of the range axis...
        // final ValueAxis rangeAxis = plot.getRangeAxis();
        // rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // rangeAxis.setLowerMargin(0.15);
        // rangeAxis.setUpperMargin(0.15);

        return chart;

    }

}