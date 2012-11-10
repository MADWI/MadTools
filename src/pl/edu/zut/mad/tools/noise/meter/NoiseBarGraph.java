package pl.edu.zut.mad.tools.noise.meter;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;

import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;

import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import pl.edu.zut.mad.tools.R;

import android.content.Context;
import android.graphics.Color;

public class NoiseBarGraph {

	private GraphicalView view;

	private TimeSeries dataset = new TimeSeries("");
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	// This will be used to customize line 1

	public NoiseBarGraph(Context ctx) {
		// Add single dataset to multiple dataset
		dataset.setTitle(ctx.getString(R.string.noise_bar_title));
		mDataset.addSeries(dataset);

		mRenderer.setXTitle("t,s");
		mRenderer.setYTitle("dB");
		mRenderer.setBarSpacing(0.5f);
		mRenderer.setAxesColor(Color.WHITE);
		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setYAxisMax(130.0f);
		mRenderer.setYAxisMin(30.0f);

		// Customize bar 1
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setDisplayChartValues(false);
		renderer.setColor(Color.YELLOW);
		mRenderer.addSeriesRenderer(renderer);

	}

	public GraphicalView getView(Context ctx) {

		view = ChartFactory.getBarChartView(ctx, mDataset, mRenderer, Type.DEFAULT);
		return view;

	}

	public void addNewPoints(GraphPoint p) {
		dataset.add(p.getX(), p.getY());
	}

}
