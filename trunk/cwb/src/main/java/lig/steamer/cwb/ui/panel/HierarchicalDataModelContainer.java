package lig.steamer.cwb.ui.panel;

import java.util.Collection;
import java.util.LinkedList;

import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.util.BeanItemContainer;

public class HierarchicalDataModelContainer<BEANTYPE> extends
		BeanItemContainer<BEANTYPE> implements Hierarchical {

	private static final long serialVersionUID = 1L;

	private Object parentPID;

	public HierarchicalDataModelContainer(Class<BEANTYPE> type, Object parentPID)
			throws IllegalArgumentException {
		super(type);
		this.parentPID = parentPID;
	}

	@Override
	public Collection<?> getChildren(Object itemId) {
		LinkedList<Object> children = new LinkedList<Object>();

        // This implementation has O(n^2) complexity when
        // painting the tree, so it's really inefficient.
        for (Object candidateId: getItemIds()) {
            Object parentRef = getItem(candidateId).
                    getItemProperty(parentPID).getValue();
            if (parentRef == itemId)
                children.add(candidateId);
        }
        
        if (children.size() > 0)
            return children;
        else
            return null;
	}

	@Override
	public Object getParent(Object itemId) {
		return getItem(itemId).getItemProperty(parentPID).getValue();
	}

	@Override
	public Collection<?> rootItemIds() {
		LinkedList<Object> result = new LinkedList<Object>();
        for (Object candidateId: getItemIds()) {
            Object parentRef = getItem(candidateId).
                    getItemProperty(parentPID).getValue();
            if (parentRef == null)
                result.add(candidateId);
        }
        
        if (result.size() > 0)
            return result;
        else
            return null;
	}

	@Override
	public boolean setParent(Object itemId, Object newParentId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Not implemented here");
	}

	@Override
	public boolean areChildrenAllowed(Object itemId) {
		return hasChildren(itemId);
	}

	@Override
	public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Not implemented here");
	}

	@Override
	public boolean isRoot(Object itemId) {
		return getItem(itemId).getItemProperty(parentPID).getValue() == null;
	}

	@Override
	public boolean hasChildren(Object itemId) {
		for (Object candidateId : getItemIds()) {
			Object parentRef = getItem(candidateId).getItemProperty(parentPID)
					.getValue();
			if (parentRef == itemId)
				return true;
		}
		return false;
	}

}
