package com.youngbingdong.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ybd
 * @date 18-11-21
 * @contact yangbingdong1994@gmail.com
 *
 * List分页
 */
public class Pager<T> {

    private List<T> list;
    private int pageSize;
    private int totalPage;
    private int size;

    private Pager(List<T> list, int pageSize) {
        this.list = list;
        this.pageSize = pageSize;
        this.size = list.size();
        this.totalPage = calculateTotalPage(this.size, this.pageSize);
    }

    public void doPage(Consumer<List<T>> consumer) {
        for (int i = 0; i < totalPage; i++) {
            if (i == totalPage - 1) {
                consumer.accept(list.subList(i * pageSize, size));
                break;
            }
            consumer.accept(list.subList(i * pageSize, i * pageSize + pageSize));
        }
    }

    public <R> List<R> doFunction(Function<List<T>, List<R>> function) {
        List<R> rs = new ArrayList<>(size);
        for (int i = 0; i < totalPage; i++) {
            if (i == totalPage - 1) {
                rs.addAll(function.apply(list.subList(i * pageSize, size)));
                break;
            }
            rs.addAll(function.apply(list.subList(i * pageSize, i * pageSize + pageSize)));
        }
        return rs;
    }

    public static <T> Pager<T> of(List<T> list, int pageSize) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException();
        }
        return new Pager<>(list, pageSize);
    }

	public static int calculateTotalPage(int total, int pageSize) {
		return (total + pageSize - 1) / pageSize;
	}
}
