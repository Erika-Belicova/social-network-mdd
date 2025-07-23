export interface CommentResponseDTO {
  id: number;
  postId: number;
  userId: number;
  username: string;
  content: string;
  createdAt: Date;
  updatedAt: Date;
}