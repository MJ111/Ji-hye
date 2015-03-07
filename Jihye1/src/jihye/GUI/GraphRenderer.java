package jihye.GUI;
import java.awt.*;

import org.jfree.chart.renderer.category.*;

//그래프 렌더링하는 클래스인 BarRenderer를 상속받아서 그래프 색을 결정하는 부분만 Override했음.
@SuppressWarnings("serial")
public class GraphRenderer extends BarRenderer {

	//정답의 색
	private Paint correctColor = new Color(192,80,77);
	//오답의 색
	private Paint incorrectColor =  new Color(79,129,189);
	
	private int correctColumn;
	
	public GraphRenderer(int correctColumn)
	{
		this.correctColumn = correctColumn;		
	}
	
	public Paint getItemPaint(final int row, final int column) {
		if(column == correctColumn) return correctColor;
		else return incorrectColor;	
		
	}

}
