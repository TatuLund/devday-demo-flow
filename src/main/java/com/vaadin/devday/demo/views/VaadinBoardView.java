package com.vaadin.devday.demo.views;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = VaadinBoardView.ROUTE, layout = MainLayout.class)
@PageTitle(VaadinBoardView.TITLE)
public class VaadinBoardView extends VerticalLayout {
    public static final String ROUTE = "board";
    public static final String TITLE = "Board";
    Board board = new Board();
	private Component pieChart1;
	private Component pieChart2;
	private Component columnChart;
    
    public VaadinBoardView(){
        setSizeFull();
        pieChart1 = ChartUtil.getPieChart("2010");
        pieChart2 = ChartUtil.getPieChart("2018");
        board.addRow(pieChart1, pieChart2);
        columnChart = ChartUtil.getColumnChart();
        board.addRow(columnChart);
        board.setSizeFull();
        add(board);
    }

}
