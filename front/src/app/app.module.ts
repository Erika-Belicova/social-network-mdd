import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { MeComponent } from './features/user/me/me.component';
import { SubscriptionsComponent } from './features/user/subscriptions/subscriptions.component';
import { PostListComponent } from './features/posts/post-list/post-list.component';
import { PostDetailComponent } from './features/posts/post-detail/post-detail.component';
import { PostFormComponent } from './features/posts/post-form/post-form.component';
import { CommentsComponent } from './features/posts/comments/comments.component';
import { TopicListComponent } from './features/topics/topic-list/topic-list.component';
import { NavbarComponent } from './shared/navbar/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NotFoundComponent,
    LoginComponent,
    RegisterComponent,
    MeComponent,
    SubscriptionsComponent,
    PostListComponent,
    PostDetailComponent,
    PostFormComponent,
    CommentsComponent,
    TopicListComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
