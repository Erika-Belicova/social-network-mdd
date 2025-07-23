import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/components/home/home.component';
import { NotFoundComponent } from './pages/components/not-found/not-found.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { MeComponent } from './features/user/components/me/me.component';
import { PostListComponent } from './features/posts/components/post-list/post-list.component';
import { PostDetailComponent } from './features/posts/components/post-detail/post-detail.component';
import { PostFormComponent } from './features/posts/components/post-form/post-form.component';
import { TopicListComponent } from './features/topics/components/topic-list/topic-list.component';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'posts', component: PostListComponent },
  { path: 'posts/create', component: PostFormComponent },
  { path: 'posts/:id', component: PostDetailComponent },
  { path: 'topics', component: TopicListComponent },
  { path: 'me', component: MeComponent },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
