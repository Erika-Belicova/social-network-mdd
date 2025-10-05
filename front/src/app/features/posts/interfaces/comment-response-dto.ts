export interface CommentResponseDTO {
  id: number;
  post_id: number;
  user_id: number;
  username: string;
  content: string;
  created_at: string;
  updated_at: string;
}