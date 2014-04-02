package com.example.android.BluetoothChat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ChartFragment extends Fragment{
  private static final String TIME = "H:mm:ss";
  private static final int TEN_SEC = 10000;
  private static final int TWO_SEC = 2000;
 // private static final float RATIO = 0.618033988749895f;
  
  private static Random RAND = new Random();
  private XYMultipleSeriesRenderer mRenderer;
  private XYMultipleSeriesDataset mDataset;
  private GraphicalView mChartView;

  private double mLastItemChange;
  private int mItemIndex;
  private static final String[] ITEMS = { "A", "B", "C", "D", "E", "F" };
  private static final int[] THRESHOLD_COLORS = { Color.RED, Color.YELLOW, Color.GREEN };
  private static final String[] THRESHOLD_LABELS = { "Bad", "Good", "Excellent" };
  private static final int[] THRESHOLD_VALUES = { 30, 60, 80 };

  private TimeSeries[] mThresholds;
  private XYSeriesRenderer[] mThresholdRenderers;
  private HashMap<String, TimeSeries> mSeries;
  private ArrayList<String> mItems;
  private int mYAxisPadding = 5;
  private double mYAxisMin = Double.MAX_VALUE;
  private double mYAxisMax = Double.MIN_VALUE;
  private double mZoomLevel = 1;

  public Queue<int[]> packetQueue = new LinkedList<int[]> ();
  int[] packet = null;//{0,0,0,0,0, 0,0,0,0,0, 10,15,20,15,0, 0,0,0,0,-10, 30,90,10,-30,0, 0,0,0,0,0, 0,10,15,18,22, 25,24,22,18,10, 0,0,0,5,0, 0,0,0,0,0};
  int index = 0;
  
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    mDataset = new XYMultipleSeriesDataset();
    mRenderer = new XYMultipleSeriesRenderer();
    
    mRenderer.setLabelsColor(Color.LTGRAY);
    mRenderer.setAxesColor(Color.LTGRAY);
    mRenderer.setGridColor(Color.rgb(136, 136, 136));
    mRenderer.setBackgroundColor(Color.BLACK);
    mRenderer.setApplyBackgroundColor(true);
    
    mRenderer.setLegendTextSize(20);
    mRenderer.setLabelsTextSize(20);
    mRenderer.setPointSize(8);
    mRenderer.setMargins(new int[] { 60, 60, 60, 60 });
    
    mRenderer.setFitLegend(true);
    mRenderer.setShowGrid(true);
    mRenderer.setZoomEnabled(true);
    mRenderer.setExternalZoomEnabled(true);
    mRenderer.setAntialiasing(true);
    mRenderer.setInScroll(true);
    
    mLastItemChange = new Date().getTime();
    mItemIndex = Math.abs(RAND.nextInt(ITEMS.length));
    mItems = new ArrayList<String>();
    mSeries = new HashMap<String, TimeSeries>();
  }
  
  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    if (Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation) {
      mYAxisPadding = 9;
      mRenderer.setYLabels(15);
    }
    
    final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_chart, container, false);
    mChartView = ChartFactory.getTimeChartView(getActivity(), mDataset, mRenderer, TIME);
    view.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

    return view;
  }
  
  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mThresholds = new TimeSeries[3];
    mThresholdRenderers = new XYSeriesRenderer[3];
    
    
    for (int i = 0; i < THRESHOLD_COLORS.length; i++) {
      mThresholdRenderers[i] = new XYSeriesRenderer();
      mThresholdRenderers[i].setColor(THRESHOLD_COLORS[i]);
      mThresholdRenderers[i].setLineWidth(3);

      mThresholds[i] = new TimeSeries(THRESHOLD_LABELS[i]);
      final long now = new Date().getTime();
      mThresholds[i].add(new Date(now - 1000 * 60 * 10), THRESHOLD_VALUES[i]);
      mThresholds[i].add(new Date(now + 1000 * 60 * 10), THRESHOLD_VALUES[i]);

      mDataset.addSeries(mThresholds[i]);
      mRenderer.addSeriesRenderer(mThresholdRenderers[i]);
    }
    
    mTimer.start();

  }
  
  @Override
  public void onStop() {
    super.onStop();
    if (null != mTimer) {
      mTimer.cancel();
    }
  }
  
  public void addPointToChart(double value) {
    //value = randomValue();
    if (mYAxisMin > value) mYAxisMin = value;
    if (mYAxisMax < value) mYAxisMax = value;

    final Date now = new Date();
    final long time = now.getTime();

    /*if (time - mLastItemChange > 10000) {
      mLastItemChange = time;
      mItemIndex = Math.abs(RAND.nextInt(ITEMS.length));
    }*/

    final String item = ITEMS[mItemIndex];
    final int lastItemIndex = mItems.lastIndexOf(item);
    mItems.add(item);

    if (lastItemIndex == -1) {
      addSeries(item, now, value, item);
    }
    mSeries.get(item).add(now, value);
   /* if (lastItemIndex > -1) {
      boolean otherItemBetween = false;
      for (int i = lastItemIndex + 1; i < mItems.size(); i++) {
        if (!item.equals(mItems.get(i))) {
          otherItemBetween = true;
          break;
        }
      }
      if (otherItemBetween) {
        addSeries(null, now, value, item);
      }
      else {
        mSeries.get(item).add(now, value);
      }
    }
    else {
      addSeries(item, now, value, item);
    }
    */

    scrollGraph(time);
    mChartView.repaint();
  }
  
  private void addSeries(final String title, final Date time, final double value, final String item) {
    for (int i = 0; i < THRESHOLD_COLORS.length; i++) {
      mThresholds[i].add(new Date(time.getTime() + 1000 * 60 * 5), THRESHOLD_VALUES[i]);
    }

    final TimeSeries series = new TimeSeries(title);
    series.add(time, value);
    mSeries.put(item, series);
    mDataset.addSeries(series);
    mRenderer.addSeriesRenderer(getSeriesRenderer(Color.GREEN));
  }

  
  // change the last parameter in the CountDownTimer to change the plotting speed  
  private final CountDownTimer mTimer = new CountDownTimer(15 * 60 * 1000, 200) {
	@Override
    public void onTick(final long millisUntilFinished) {
		// Retrieve next available packet
		if (packet == null)
			packet = packetQueue.poll();
		
		// Plot the packet if available
		if (packet != null) {
			addPointToChart(packet[index]);
			index++;
			
			// Plotting the packet finished. Reset.
			if (index == 50) {
				index = 0;
				packet = null;
			}
		}
    }

    @Override
    public void onFinish() {}
  };
  
  private double randomValue() {
    final int value = Math.abs(RAND.nextInt(32));
    final double percent = (value * 100) / 31.0;
    return ((int) (percent * 10)) / 10.0;
  }
 
  
  private XYSeriesRenderer getSeriesRenderer(final int color) {
    final XYSeriesRenderer r = new XYSeriesRenderer();
    r.setDisplayChartValues(true);
    r.setChartValuesTextSize(30);
    r.setPointStyle(PointStyle.CIRCLE);
    r.setColor(color);
    r.setFillPoints(true);
    r.setLineWidth(4);
    return r;
  }
  
  private void scrollGraph(final long time) {
    final double[] limits = new double[] { time - TEN_SEC * mZoomLevel, time + TWO_SEC * mZoomLevel, mYAxisMin - mYAxisPadding,
        mYAxisMax + mYAxisPadding };
    mRenderer.setRange(limits);
  }

	public interface OnChartListener {
		// TODO: Update argument type and name
		public void OnChartListener(int data);
	}
}
