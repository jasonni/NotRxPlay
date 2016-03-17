package com.example.jasonni.rxplay;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    static class Student {
        String name;
        Course[] courses;

        public Student(String name, Course[] courses) {
            this.name = name;
            this.courses = courses;
        }
    }

    static class Course {
        String name;

        public Course(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Course c1 = new Course("1");
        Course c2 = new Course("2");
        Course c3 = new Course("3");
        Course c4 = new Course("4");
        Course c5 = new Course("5");

        Student A = new Student("A", new Course[]{c1, c2});
        Student B = new Student("B", new Course[]{c2, c4});

        final Student[] students = {A, B};

        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course name) {
                System.out.println(name.name);
            }
        };

//        Observable.create(new Observable.OnSubscribe<Course>() {
//            @Override
//            public void call(Subscriber<? super Course> subscriber) {
//                subscriber.onStart();
//                for (Student student : students) {
//                    for (Course course : student.courses) {
//                        subscriber.onNext(course);
//                    }
//                }
//                subscriber.onCompleted();
//            }
//        }).subscribe(subscriber);

//        Observable.from(students)
//                .map(new Func1<Student, Course[]>() {
//                    @Override
//                    public Course[] call(Student student) {
//                        return student.courses;
//                    }
//                })
//                .subscribe(subscriber);

//        Observable.from(students)
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        return Observable.from(student.courses);
//                    }
//                }).subscribe(subscriber);

    }

}
