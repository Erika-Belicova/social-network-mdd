import { TopicDTO } from "../../topics/interfaces/TopicDTO";

export interface UserDTO {
  id: number;
  username: string;
  email: string;
  topics: TopicDTO[];
}