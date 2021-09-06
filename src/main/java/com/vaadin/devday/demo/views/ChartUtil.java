package com.vaadin.devday.demo.views;

import java.util.Random;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.ChartOptions;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.charts.model.DataLabels;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.DataSeriesItem3d;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.charts.model.Labels;
import com.vaadin.flow.component.charts.model.Lang;
import com.vaadin.flow.component.charts.model.LayoutDirection;
import com.vaadin.flow.component.charts.model.Legend;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Marker;
import com.vaadin.flow.component.charts.model.PlotLine;
import com.vaadin.flow.component.charts.model.PlotOptionsBubble;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.charts.model.PlotOptionsColumnrange;
import com.vaadin.flow.component.charts.model.PlotOptionsGauge;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.VerticalAlign;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.charts.model.style.Color;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;

public class ChartUtil {
    private static String color0 = "#E6C317";
    private static String color1 = "#7997F2";
    private static String color2 = "#C2F261";
    private static String color3 = "#D96C57";
    private static String color4 = "#5C81CC";
    private static String color5 = "#39BF87";
    private static String color6 = "#E695AC";
    private static String color7 = "#75D0FF";
    private static String color8 = "#904FB0";
    private static String color9 = "#A6EDD9";

    public static Div getColumnChart() {
        // Wrap Chart in Div, otherwise it wont reflow in Board
        Div div = new Div();
        div.setSizeFull();
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setSizeFull();
        
        Configuration conf = chart.getConfiguration();

        conf.setTitle("Monthly Average Rainfall");
        conf.setSubTitle("Source: WorldClimate.com");

        XAxis x = new XAxis();
        x.setCategories("Jan<br>2020", "Feb<br>2020", "Mar<br>2020", "Apr<br>2020", "May<br>2020", "Jun<br>2020", "Jul<br>2020", "Aug<br>2020",
                "Sep<br>2020", "Oct<br>2020", "Nov<br>2020", "Dec<br>2020");
        conf.addxAxis(x);
        Labels labs = x.getLabels();
        labs.setUseHTML(true);
        x.setLabels(labs);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Rainfall (mm)");
        conf.addyAxis(y);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setAlign(HorizontalAlign.LEFT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(100);
        legend.setY(70);
        legend.setFloating(true);
        legend.setShadow(true);
        conf.setLegend(legend);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setShowInLegend(true);

        conf.setPlotOptions(plot);

        ListSeries tokyo = new ListSeries("Tokyo", 49.9, 71.5, 106.4, 129.2,
                144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4);
        conf.addSeries(tokyo);
        PlotOptionsColumn options = new PlotOptionsColumn();
//        options.setColorIndex(6);
        options.setBorderRadius(4.0);
        DataLabels labels = new DataLabels();
        labels.setEnabled(true);
        options.setDataLabels(labels);
        tokyo.setPlotOptions(options);
        options.setColor(new SolidColor(color6));

        ListSeries newYork = new ListSeries("New York", 83.6, 78.8, 98.5, 93.4,
                106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3);
        PlotOptionsColumn optionsNY = new PlotOptionsColumn();
        optionsNY.setCrisp(true);
        optionsNY.setColor(new SolidColor(color1));
        newYork.setPlotOptions(optionsNY);
        PlotLine plotLine = new PlotLine();
        plotLine.setValue(40);
        conf.getyAxis().addPlotLine(plotLine);
        conf.addSeries(newYork);
        conf.addSeries(new ListSeries("London", 48.9, 38.8, 39.3, 41.4, 47.0,
                48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2));
        conf.addSeries(new ListSeries("Berlin", 42.4, 33.2, 34.5, 39.7, 52.6,
                75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1));
        div.add(chart);

        // This is partial workaround for missing setLang(..) method, this won't
        // work in npm mode at all.
        // chart.getElement().executeJs("Highcharts.setOptions({\r\n" +
        // " lang: {\r\n" +
        // " shortMonths: [\r\n" +
        // " 'Jan', 'Fév', 'Mar', 'Avr',\r\n" +
        // " 'Mai', 'Jui', 'Jul', 'Aoû',\r\n" +
        // " 'Sep', 'Oct', 'Nov', 'Déc'\r\n" +
        // " ],\r\n" +
        // " shortWeekdays: [\r\n" +
        // " 'Dim', 'Lun', 'Mar', 'Mer',\r\n" +
        // " 'Jeu', 'Ven', 'Sam'\r\n" +
        // " ]\r\n" +
        // " }\r\n" +
        // "});");
        Lang lang = new Lang();
        ChartOptions.get().setLang(lang);
        String[] months = {"Jan","Fév","Mar","Avr","Mai","Jui","Jul","Aoû","Sep","Oct","Nov","Déc"}; 
        lang.setShortMonths(months);
        return div;
    }

    public static Div getBubbleChart() {
        Div div = new Div();
        Chart chart = new Chart(ChartType.BUBBLE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle((String) null);

        DataSeries dataSeries = createDataSeries("main", 2);

        PlotOptionsBubble opts = new PlotOptionsBubble();
        opts.setMaxSize("120");
        opts.setMinSize("3");
        opts.setColor(SolidColor.CORNFLOWERBLUE);

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        tooltip.setFormatter(
                "function() { var s='<span style=\"font-weight:bold; color: blue\">'+this.series.name+'</span><br><i>'+this.point.name+'</i><br>';if (this.series.name.includes('sub')) { s+='X: <b>'+this.x+'</b><br>Y: <b>'+this.y+'</b>'; } else if (this.series.name.includes('Drill')) {s+='Click me';};return s;}");
        conf.setTooltip(tooltip);

        conf.setPlotOptions(opts);

        conf.addSeries(dataSeries);

        XAxis axis = new XAxis();
        PlotLine plotLine = new PlotLine();
        plotLine.setValue(50);
        axis.addPlotLine(plotLine);
        conf.addxAxis(axis);
        conf.getChart().setStyledMode(false);
//        conf.getScrollbar().setEnabled(true);

        DataSeries dataSeries2 = new DataSeries("Drill down items");
        dataSeries2.addItemWithDrilldown(item(13, 30, 10, 6, "drill"),
                createDataSeries2("sub_1", 3));
        dataSeries2.addItemWithDrilldown(item(23, 20, -10, 6, "drill"),
                createDataSeries2("sub_2", 4));
        dataSeries2.addItemWithDrilldown(item(23, 40, 10, 6, "drill"),
                createDataSeries2("sub_3", 5));
        // Commented code demos how lazy loading drills could work, but it is
        // not
        // working due bugs in Charts, I have added bug reports in tracker
        // List<DataSeries> drills = new ArrayList<>();
        // drills.add(createDataSeries2("sub_1", 3));
        // drills.add(createDataSeries2("sub_2", 4));
        // drills.add(createDataSeries2("sub_3", 5));
        // dataSeries2.addItemWithDrilldown(item(13, 30, 10, 6, "drill"),
        // drills.get(0));
        // dataSeries2.addItemWithDrilldown(item(23, 20, -10, 6, "drill"),
        // drills.get(1));
        // dataSeries2.addItemWithDrilldown(item(23, 40, 10, 6, "drill"),
        // drills.get(2));
        // opts = new PlotOptionsBubble();
        opts.setDisplayNegative(false);
        dataSeries2.setPlotOptions(opts);
        conf.addSeries(dataSeries2);

        NumberField min = new NumberField();
        min.setHasControls(true);
        min.setStep(1);
        min.setValue(0d);
        NumberField max = new NumberField();
        max.setStep(1);
        max.setHasControls(true);
        max.setValue(100d);
        min.addValueChangeListener(event -> {
            axis.setExtremes(min.getValue(), max.getValue(), true, true);
        });
        max.addValueChangeListener(event -> {
            axis.setExtremes(min.getValue(), max.getValue(), true, true);
        });
        Button reset = new Button("reset");
        reset.addClickListener(event -> {
            min.setValue(0d);
            max.setValue(100d);
        });
        div.add(chart, min, max, reset);

        chart.addPointClickListener(event -> {
            if (event.getItem().getId().equals("data")) {
                min.setValue(event.getxAxisValue() - 10);
                max.setValue(event.getxAxisValue() + 10);
//                conf.getScrollbar().setEnabled(true);
            }
        });

        chart.setDrilldownCallback(event -> {
            DataSeriesItem item = event.getItem();
            return updateDataSeries(item.getId(), 3);
        });
        chart.addChartDrillupAllListener(event -> {
            min.setValue(0d);
            max.setValue(100d);            
//            conf.getScrollbar().setEnabled(false);
        });

        // chart.addDrilldownListener(event -> {
        // String drillDown = event.getDrilldown();
        // for (DataSeries drill : drills) {
        // if (drill.getId().equals(drillDown)) {
        // updateDataSeries(drill, drill.getId(), 3);
        // drill.updateSeries();
        // }
        // }
        // });

        return div;
    }

    private static DataSeries createDataSeries(String id, int colorIndex) {
        DataSeries dataSeries = new DataSeries("Series "+colorIndex);
        dataSeries.setId(id);
        dataSeries.add(item(9, 81, 13, colorIndex));
        dataSeries.add(item(98, 5, 39, colorIndex));
        dataSeries.add(item(51, 50, 23, colorIndex));
        dataSeries.add(item(41, 22, -36, colorIndex));
        dataSeries.add(item(58, 24, -30, colorIndex));
        dataSeries.add(item(78, 37, -16, colorIndex));
        dataSeries.add(item(55, 56, 3, colorIndex));
        dataSeries.add(item(18, 45, 20, colorIndex));
        dataSeries.add(item(42, 44, -22, colorIndex));
        dataSeries.add(item(3, 52, 9, colorIndex));
        dataSeries.add(item(31, 18, 47, colorIndex));
        dataSeries.add(item(79, 91, 13, colorIndex));
        dataSeries.add(item(93, 23, -27, colorIndex));
        dataSeries.add(item(44, 83, -28, colorIndex));
        return dataSeries;
    }

    private static DataSeries createDataSeries2(String id, int colorIndex) {
        DataSeries dataSeries = new DataSeries("Drill down: " + id);
        dataSeries.setId(id);
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            dataSeries
                    .add(item(rand.nextInt(200) - 100, rand.nextInt(200) - 100,
                            rand.nextInt(200) - 100, colorIndex, i, "data"));
        }
        dataSeries.addItemWithDrilldown(item(23, 40, 10, 6),
                createDataSeries("sub_" + id, 5));
        return dataSeries;
    }

    private static DataSeries updateDataSeries(String id, int colorIndex) {
        DataSeries dataSeries = new DataSeries();
        System.out.println("Updating series " + id);
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            dataSeries
                    .add(item(rand.nextInt(200) - 100, rand.nextInt(200) - 100,
                            rand.nextInt(200) - 100, colorIndex, i, "data"));
        }
        dataSeries.addItemWithDrilldown(item(23, 40, 10, 6),
                createDataSeries("sub_" + id, 5));
        return dataSeries;
    }

    public static DataSeriesItem item(int x, int y, int z, int colorIndex) {
        return item(x, y, z, colorIndex, -1, "data");
    }

    public static DataSeriesItem item(int x, int y, int z, int colorIndex,
            String id) {
        return item(x, y, z, colorIndex, -1, id);
    }

    public static DataSeriesItem item(int x, int y, int z, int colorIndex,
            int index, String id) {
        DataSeriesItem3d dataSeriesItem = new DataSeriesItem3d();
        dataSeriesItem.setId(id);
        dataSeriesItem.setX(x);
        dataSeriesItem.setY(y);
        dataSeriesItem.setZ(z);
        if (colorIndex == 6) {
            dataSeriesItem.setColor(SolidColor.DARKGOLDENROD);
        } else if (colorIndex == 5) {
            dataSeriesItem.setColor(SolidColor.CADETBLUE);
        } else if (colorIndex == 4) {
            dataSeriesItem.setColor(SolidColor.THISTLE);
        } else if (colorIndex == 3) {
                dataSeriesItem.setColor(SolidColor.ORANGERED);
        } else {
            dataSeriesItem.setColor(SolidColor.BROWN);
        }
        DataLabels labels = new DataLabels();
        labels.setEnabled(true);
        dataSeriesItem.setDataLabels(labels);
        if (index > -1) {
            dataSeriesItem.setName("Item " + index);
        } else {
            dataSeriesItem.setName("Main");
        }
        return dataSeriesItem;
    }

    public static Div getPieChart(String year) {
        Div div = new Div();
        div.setSizeFull();
        Chart chart = new Chart(ChartType.PIE);
        chart.setSizeFull();

        Configuration conf = chart.getConfiguration();

        // conf.setTitle("Browser market shares, "+year);
        // conf.setSubTitle("at a specific website");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        tooltip.setPointFormat("Share: <b>{point.percentage}%</b>");
        conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setInnerSize("60%");
        plotOptions.setColors(new SolidColor(color0),new SolidColor(color1),new SolidColor(color2),new SolidColor(color3),new SolidColor(color4),new SolidColor(color5),new SolidColor(color6));
        conf.setPlotOptions(plotOptions);

        if ("2010".equals(year)) {
            DataSeries series = new DataSeries();
            series.add(new DataSeriesItem("Firefox", 45.0));
            series.add(new DataSeriesItem("IE", 26.8));
            DataSeriesItem chrome = new DataSeriesItem("Chrome", 12.8);
            chrome.setSliced(true);
            chrome.setSelected(true);
            series.add(chrome);
            series.add(new DataSeriesItem("Safari", 8.5));
            series.add(new DataSeriesItem("Opera", 6.2));
            series.add(new DataSeriesItem("Others", 0.7));
            conf.setSeries(series);
        } else {
            DataSeries series = new DataSeries();
            DataSeriesItem chrome = new DataSeriesItem("Chrome", 61.0);
            chrome.setSliced(true);
            chrome.setSelected(true);
            series.add(chrome);
            series.add(new DataSeriesItem("Internet Explorer", 11.0));
            series.add(new DataSeriesItem("Firefox", 10.0));
            series.add(new DataSeriesItem("Edge", 4.0));
            series.add(new DataSeriesItem("Safari", 4.0));
            series.add(new DataSeriesItem("Sogou Explorer", 1.0));
            series.add(new DataSeriesItem("Opera", 6.0));
            series.add(new DataSeriesItem("QQ", 1.0));
            series.add(new DataSeriesItem("Others", 2.0));
            conf.setSeries(series);
        }

        div.add(chart);
        return div;
    }

}
