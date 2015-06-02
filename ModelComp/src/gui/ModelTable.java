package gui;

import java.util.*;
import javax.swing.table.*;

//������ ����� �������, ����� ������
//� �������� �� ���� ����� ComputingModel
//������� ������������ setInitialModel � ���������� setDataSource ����� !!
public class ModelTable extends AbstractTableModel {
	// ��� �� ������ �������� ����� ���������
	private ArrayList columnNames = new ArrayList();
	// ������ ���� ���������
	private ArrayList columnTypes = new ArrayList();
	// ������ ��������� ���������
	private ArrayList columnEditable = new ArrayList();
	// ������� ��� ��������� ����� � ���� �����
	private ArrayList data = new ArrayList();
		
	// ����������� �������� ���������� ��������� �����������
	public ModelTable(boolean editable) {
		this.editable = editable;
	}
	private boolean editable;
	
	// ������� �����
	public int getRowCount() {
		synchronized(data){
			return data.size();
		}	
	}
	// ������� ���������
	public int getColumnCount() {
		return columnNames.size();
	}
	// ��� ����� ���������
	public Class getColumnClass(int column){
		  return (Class)columnTypes.get(column);
	}
	// ����� ���������
	public String getColumnName(int column) {
			return (String)columnNames.get(column);
	}
	// ���� � ������
	public Object getValueAt(int row, int column) {
		synchronized(data){
			return ((ArrayList) data.get(row)).get(column);
		}	
	}
	// ��������� �����������
	public boolean isCellEditable(int row, int column){ 
		if (!(Boolean)columnEditable.get(column)) return false;
		return editable;
	}
	// ����� �������� ������
	public void setValueAt(Object value, int row, int column){
		synchronized(data){
			((ArrayList) data.get(row)).set(column, value);
		}
	}
		
	// ���������� �������� ��� ����� �� ��������� ��� ����� � ��������
	// columnInfor[i][0] = "......"   - ����� �������
	// columnInfor[i][1] = "B.."/"S.."/"I.."/".."  - ��� ������� - Boolean/String/Integer/Object
	// columnInfor[i][2] = "E.."/"N.."  - ���������� ������� - ��������/�� ��������
	public void setInitialModel(String [][] columnInfor) {
		Class type;
		// �������� �������� ���
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
		// ����������� ��� ���� � �������� �����
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
					// ����������� ��� ����� �����
					fireTableRowsInserted(data.size()-1, data.size()-1);
				}
			}
		//} else {
	//		fireTableStructureChanged();
		}
	}
	
}
