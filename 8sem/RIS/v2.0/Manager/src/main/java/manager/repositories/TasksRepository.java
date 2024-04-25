package manager.repositories;

import manager.models.JsonBodies.ResultBody;
import manager.models.JsonBodies.ResultData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends MongoRepository<ResultData, String> {

}
