import { TopicDTO } from "../../topics/interfaces/topic-dto";

export interface UserDTO {
  id: number;
  username: string;
  email: string;
  topics: TopicDTO[];
}