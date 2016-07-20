package com.n42.analytics.hibernate.dao;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import n42.domain.model.Service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.n42.analytics.hibernate.util.HibernateFactory;

@SuppressWarnings("unchecked")
public class ServiceDaoImpl extends AbstractDao implements ServiceDao {

	@Override
	public Service saveService(Service service) {
		service = (Service) setMetaAttributes(service);
		return (Service) super.save(service);
	}

	@Override
	public Service getServiceById(Integer id) {
		return (Service) super.findById(Service.class, id);
	}

	@Override
	public Service getServiceById(Integer id, List<Class<?>> listAssocations) {
		Service service = null;
		try {
			startOperation();
			service = (Service) session.get(Service.class, id);
			loadAssociations(service, listAssocations);

			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return service;
	}

	@Override
	public Service getServiceByName(String name) {
		Service service = null;
		List<Service> services = (List<Service>) super.findByAttribute(
				Service.class, "name", name);
		service = (CollectionUtils.isNotEmpty(services)) ? services.get(0)
				: null;
		return service;
	}

	@Override
	public Service getServiceByName(String name, List<Class<?>> associations) {
		Service service = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(Service.class)
					.addOrder(Order.desc("n42LastUpdatedDate"))
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq("name", name));
			List<Service> services = criteria.list();
			service = (CollectionUtils.isNotEmpty(services)) ? services.get(0)
					: null;
			loadAssociations(service, associations);
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return service;
	}

	@Override
	public void deleteService(Integer id) {
		super.delete(Service.class, id);
	}

	@Override
	public void updateService(Service service) {
		Service fromDb = null;
		try {
			startOperation();
			fromDb = (Service) session.get(Service.class, service.getN42Id());
			fromDb = (Service) setMetaAttributes(service);
			fromDb.setName(service.getName());
			fromDb.setDescription(service.getDescription());
			fromDb.setHostIds(service.getHostIds());
			fromDb.setActive(service.getActive());
			session.update(fromDb);
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.clear();
			HibernateFactory.close(session);
		}
	}

	@Override
	public List<Service> getAllServices() {
		List<Service> services = (List<Service>) super.findAll(Service.class);
		return services;
	}

	@Override
	public List<Service> getAllServices(List<Class<?>> associations) {
		List<Service> services = (List<Service>) super.findAll(Service.class);
		try {
			startOperation();
			Criteria criteria = session.createCriteria(Service.class)
					// .setCacheable(true)
					.addOrder(Order.desc("n42LastUpdatedDate"))
					.add(Restrictions.eq("active", true));
			services = criteria.list();
			if (CollectionUtils.isNotEmpty(associations)) {
				for (Service service : services) {
					loadAssociations(service, associations);
				}
			}

			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return services;
	}

	@Override
	public List<Service> getAllServices(String application, String tenant,
			List<Class<?>> associations) {
		List<Service> services = null;
		try {
			startOperation();
			if (StringUtils.isNotBlank(application)) {
				Criteria criteria = session
						.createCriteria(Service.class)
						// .setCacheable(true)
						.addOrder(Order.desc("n42LastUpdatedDate"))
						.add(Restrictions.eq("active", true))
						.add(Restrictions.eq("application", application));
				services = criteria.list();
			} else if (StringUtils.isNotBlank(tenant)) {
				Criteria criteria = session
						.createCriteria(Service.class)
						// .setCacheable(true)
						.addOrder(Order.desc("n42LastUpdatedDate"))
						.add(Restrictions.eq("active", true))
						.add(Restrictions.eq("tenant", tenant));
				services = criteria.list();
			} else {
				Criteria criteria = session.createCriteria(Service.class)
						// .setCacheable(true)
						.addOrder(Order.desc("n42LastUpdatedDate"))
						.add(Restrictions.eq("active", true));
				services = criteria.list();
			}
			if (CollectionUtils.isNotEmpty(associations)) {
				for (Service service : services) {
					loadAssociations(service, associations);
				}
			}

			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return services;
	}

	@Override
	public List<Service> getAllServicesByApplication(String application,
			List<Class<?>> associations) {
		List<Service> services = (List<Service>) super.findAll(Service.class);
		try {
			startOperation();
			Criteria criteria = session
					.createCriteria(Service.class)
					// .setCacheable(true)
					.addOrder(Order.desc("n42LastUpdatedDate"))
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq("application", application));
			services = criteria.list();
			if (CollectionUtils.isNotEmpty(associations)) {
				for (Service service : services) {
					loadAssociations(service, associations);
				}
			}

			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return services;
	}

	@Override
	public List<String> getAllServiceNames(String application) {
		List<String> services = new ArrayList<String>();
		try {
			startOperation();
			Criteria criteria = session
					.createCriteria(Service.class)
					// .setCacheable(true)
					.addOrder(Order.desc("n42LastUpdatedDate"))
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq("application", application))
					.setProjection(Projections.property("name"));
			services = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return services;
	}

	@Override
	public List<String> getAllGroups() {
		List<String> groups = null;
		try {
			startOperation();
			Query query = session
					.createQuery("Select serviceGroup from Service where active = true");
			groups = query.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}

		if (isNotEmpty(groups)) {
			Set<String> set = new HashSet<>(groups);
			groups = new ArrayList<>(set);
		} else {
			groups = Collections.EMPTY_LIST;
		}

		return groups;
	}

	@Override
	public List<Service> getServicesByGroup(String group) {
		return (List<Service>) super.findByAttribute(Service.class,
				"serviceGroup", group);
	}

	@Override
	public void deleteAll() {
		super.deleteAll(Service.class);
	}

	public static void main(String[] args) {
		ServiceDaoImpl serviceDaoImpl = new ServiceDaoImpl();
		serviceDaoImpl.getServiceByName("HaProxy");
	}

	private void loadAssociations(Service service, List<Class<?>> associations) {
		if (CollectionUtils.isNotEmpty(associations)) {
			for (Class<?> clazz : associations) {
				if (clazz.getName().contains("Host")) {
					Hibernate.initialize(service.getHostIds());
				}
			}
		}
	}

	public Service getServiceByVMId(int vmId) {
		Service service = null;
		List<Service> services = (List<Service>) super.findByAttribute(
				Service.class, "vmId", vmId);
		service = (CollectionUtils.isNotEmpty(services)) ? services.get(0)
				: null;
		return service;
	}

	public List<Service> getServicesByVMId(String name, int vmId) {
		List<Service> services = (List<Service>) super.findByAttribute(
				Service.class, "vmId", vmId);
		return services;
	}

	public void deleteServicesByVMId(int vmId) {
		List<Service> services = (List<Service>) super.findByAttribute(
				Service.class, "vmId", vmId);
		if (CollectionUtils.isEmpty(services)) {
			return;
		}
		for (Service service : services) {
			deleteService(service.getN42Id());
		}
	}
}
