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

import { authGuard } from './core/guards/auth.guard'; 

const routes: Routes = [
  // public routes
  { path: '', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },

  // protected routes
  { path: 'posts', component: PostListComponent, canActivate: [authGuard] },
  { path: 'posts/create', component: PostFormComponent, canActivate: [authGuard] },
  { path: 'posts/:id', component: PostDetailComponent, canActivate: [authGuard] },
  { path: 'topics', component: TopicListComponent, canActivate: [authGuard] },
  { path: 'me', component: MeComponent, canActivate: [authGuard] },

  // route not found
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
