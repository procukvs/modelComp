package gui;

import java.util.*;
import javax.swing.table.*;

//Модель даних таблиці, котра працює
//з запитами до бази даних ComputingModel
//розділяє встановлення setInitialModel і заповнення setDataSource даних !!
public class ModelTable extends AbstractTableModel {
	// Тут ми будемо зберігати назви стовпчиків
	private ArrayList columnNames = new ArrayList();
	// список типів стовпчиків
	private ArrayList columnTypes = new ArrayList();
	// список редагуємих стовпчиків
	private ArrayList columnEditable = new ArrayList();
	// сховище для отриманих даних з бази даних
	private ArrayList data = new ArrayList();
		
	// конструктор дозволяє встановити можливість редагування
	public ModelTable(boolean editable) {
		this.editable = editable;
	}
	private boolean editable;
	
	// кількість рядків
	public int getRowCount() {
		synchronized(data){
			return data.size();
		}	
	}
	// кількість стовпчиків
	public int getColumnCount() {
		return columnNames.size();
	}
	// тип даних стовпчика
	public Class getColumnClass(int column){
		  return (Class)columnTypes.get(column);
	}
	// назва стовпчика
	public String getColumnName(int column) {
			return (String)columnNames.get(column);
	}
	// данні в комірці
	public Object getValueAt(int row, int column) {
		synchronized(data){
			return ((ArrayList) data.get(row)).get(column);
		}	
	}
	// можливість редагування
	public boolean isCellEditable(int row, int column){ 
		if (!(Boolean)columnEditable.get(column)) return false;
		return editable;
	}
	// заміна значення комірки
	public void setValueAt(Object value, int row, int column){
		synchronized(data){
			((ArrayList) data.get(row)).set(column, value);
		}
	}
		
	// встановлюе початкові дані моделі не змінюються при роботі з таблицею
	// columnInfor[i][0] = "......"   - назва колонки
	// columnInfor[i][1] = "B.."/"S.."/"I.."/".."  - тип колонки - Boolean/String/Integer/Object
	// columnInfor[i][2] = "E.."/"N.."  - редагуємість колонки - Редагуєма/Не редагуєма
	public void setInitialModel(String [][] columnInfor) {
		Class type;
		// вилучаємо попередні дані
		columnNames.clear();
		columnTypes.clear();
		columnEditable.clear();
		for(int i = 0; i < columnInfor.length; i++){ 
			columnNames.add(columnInfor[i][0]);
			switch(columnInfor[i][1].charAt(0)){
				case 'B': type = Boolean.class;  break;
				case 'I': type = Integer.class;  break;
				case 'S': type = String.class; break;
				default: type = Object.class; 
			}
			columnTypes.add(type);
			columnEditable.add(columnInfor[i][2].charAt(0) == 'E');
		}
		// повідомляємо про зміни в структурі даних
		fireTableStructureChanged();
	}

	public void setDataSource(ArrayList dataIn){
		data.clear();
		//System.out.println("setDateSourse : " + dataIn.size() + "...");
		if (dataIn != null) {
			for (int i = 0; i < dataIn.size(); i++){
				//System.out.println("size = " + dataIn.size() + " i =   " + i);
				synchronized(data){
					data.add(dataIn.get(i));
					// повідомляємо про появу рядка
					fireTableRowsInserted(data.size()-1, data.size()-1);
				}
			}
		//} else {
	//		fireTableStructureChanged();
		}
	}
	
}
