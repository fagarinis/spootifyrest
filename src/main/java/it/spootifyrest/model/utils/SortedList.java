package it.spootifyrest.model.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import it.spootifyrest.model.Brano;

/**
 * una SortedList è un'estensione di ArrayList che mantiene l'ordine ascendente
 * degli oggetti quando si usa il metodo add o addAll
 * 
 * @author Federico
 * @param <T> oggetti della classe che devono necessariamente implementare
 *            Comparable
 */
public class SortedList<T> extends ArrayList<T> implements List<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// TEST
//	public static void main(String[] args) {
//		SortedList<Brano> L = new SortedList<>();
//		L.add(new Brano(5L));
//		L.add(new Brano(100L));
//		L.add(new Brano(2L));
//		L.add(new Brano(22L));
//		L.add(new Brano(2L));
//		L.add(new Brano(4L));
//		
//		System.out.println(L);
//	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean add(Object e) {
		@SuppressWarnings("unchecked")
		Comparable<T> o = (Comparable<T>) e;
		for (int i = 0; i < this.size(); i++) {
			if ((((Comparable<T>) this.get(i)).compareTo((T) o)) > 0) {
				super.add(i, (T) o);
				return true;
			}
		}
		return super.add((T) o);
	}

	@Override
	public boolean addAll(Collection c) {
		for (Object o : c) {
			this.add(o);
		}
		return true;
	}

	/**
	 * questo metodo non deve funzionare come in un ArrayList per mantenere l'ordine
	 */
	@Override
	@Deprecated
	public boolean addAll(int index, Collection c) {
		return addAll(c);
	}

	/**
	 * questo metodo non deve funzionare come in un ArrayList per mantenere l'ordine
	 */
	@Override
	@Deprecated
	public Object set(int index, Object element) {
		this.remove(index);
		return this.add(element);
	}

	/**
	 * questo metodo non deve funzionare come in un ArrayList per mantenere l'ordine
	 */
	@Override
	@Deprecated
	public void add(int index, Object element) {
		add(element);
	}

	public boolean isSorted() {
		return this.stream().sorted().collect(Collectors.toList()).equals(this);
	}

}
