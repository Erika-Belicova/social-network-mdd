import { CommentResponseDTO } from "./comment-response-dto";

export interface PostResponseDTO {
  id: number;
  topicId: number;
  userId: number;
  title: string;
  content: string;
  createdAt: Date;
  updatedAt: Date;
  comments: CommentResponseDTO[];
}