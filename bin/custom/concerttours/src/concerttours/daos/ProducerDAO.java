package concerttours.daos;

import concerttours.model.ProducerModel;

import java.util.List;

public interface ProducerDAO {
    List<ProducerModel> findProducers();

    List<ProducerModel> findProducersByCode(String code);
}
