module.exports = function(grunt) {

   grunt.initConfig( {
      //properties: {
      //   props: "grunt.properties"
      //},

      pkg: grunt.file.readJSON('package.json'),

      uglify: {
         options: {
            banner: "/*! <%= pkg.name %> <%= grunt.template.today('yyyy-mm-dd') %> */\n"
         },

         build: {
            src  : "src/<%= pkg.name %>.js",
            dest : "build/<%= pkg.name %>.min.js"
         }
      },

      // Gradle builds and runs the Selenium client (JUnit tests)
      exec: {
         "gradle-test": {
            cmd: function() {
               return "cd SeleniumTester && gradle clean test --stacktrace";
            }
         }
      },

      // The Selenium server retrieves the test page via HTTP
      "http-server": {         
         "dev": {
            root: ".",
            port: 8000,
            host: "127.0.0.1",
            cache: 0,
            showDir: true,
            autoIndex: true,
            ext: "html",
            runInBackground: true,
            openBrowser : false
         },

         // This configuration is not used by the build process.  It keeps the server running so that you can run SeleniumTester in a debugger.
         "debug": {
            root: ".",
            port: 8000,
            host: "127.0.0.1",
            cache: 0,
            showDir: true,
            autoIndex: true,
            ext: "html",
            runInBackground: false,
            openBrowser : false            
         }
      },

      // The Selenium server loads the test page and automates the browser per the requests from the Selenium client.
      // If you change the Selenium version here, be sure to update build.gradle in the SeleniumTester project.
      "start-selenium-server": {
         dev: {
            options: {
               autostop: false,
               downloadUrl: "https://selenium-release.storage.googleapis.com/3.0-beta2/selenium-server-standalone-3.0.0-beta2.jar",
               serverOptions: {},
               systemProperties: {}
            }
         }
      },

      "stop-selenium-server": {
         dev: {}
      }
   } );

   grunt.loadNpmTasks( "grunt-exec" );
   grunt.loadNpmTasks( "grunt-http-server" );
   grunt.loadNpmTasks( "grunt-selenium-server" );
   //grunt.loadNpmTasks( "grunt-properties-reader" );
   grunt.loadNpmTasks( "grunt-contrib-uglify" );

   grunt.registerTask( "test", "Run unit tests", function() {
      grunt.task.run( "http-server:dev", "start-selenium-server:dev", "exec:gradle-test", "stop-selenium-server:dev" );
   } );
};
