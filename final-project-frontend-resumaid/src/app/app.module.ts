import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { UserService } from './services/user.service';
import { MainComponent } from './components/main/main.component';
import { JwtService } from './services/jwt.service';
import { jwtInterceptor } from './jwt.interceptor';
import { enterRestricted } from './route.guards';
import { CreateComponent } from './components/create/create.component';
import { TemplateselectorComponent } from './components/create/templateselector.component';
import { ResumebuilderComponent } from './components/create/resumebuilder.component';
import { ResumepreviewComponent } from './components/create/resumepreview.component';
import { ResumeService } from './services/resume.service';
import { ResumeStore } from './services/resume.store';
import { OllamaService } from './services/ollama.service';
import { OllamaComponent } from './components/create/ollama.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

const appRoutes: Routes = [
  {path: '', component: RegistrationComponent},
  {path: 'login', component: LoginComponent},
  {path: 'main/:id', component: MainComponent, canActivate: [enterRestricted]},
  {path: 'create/:id', component: CreateComponent, canActivate: [enterRestricted]},
  {path: '**', redirectTo:'/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    MainComponent,
    CreateComponent,
    TemplateselectorComponent,
    ResumebuilderComponent,
    ResumepreviewComponent,
    OllamaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, {useHash: true})
  ],
  providers: [UserService,
    JwtService, 
    ResumeService,
    ResumeStore,
    OllamaService,
    { provide: HTTP_INTERCEPTORS, useClass: jwtInterceptor, multi: true },
    provideAnimationsAsync() // All classes have this
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
