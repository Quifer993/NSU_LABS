package manager.repositories;

import manager.models.JsonBodies.CrackHashManagerRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksNotReadyRepository extends MongoRepository<CrackHashManagerRequest, String> {

}
