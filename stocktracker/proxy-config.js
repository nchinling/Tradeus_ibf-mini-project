module.exports = [
  {
    context: [ "/api/**" ],
    target: "http://localhost:8080",
    secure: false
  }
]

// module.exports = [
//   {
//     context: ["/api/**"],
//     target: "http://localhost:8080",
//     secure: false,
//     changeOrigin: true,
//     headers: {
//       "Access-Control-Allow-Origin": "http://localhost:4200",
//       "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, OPTIONS",
//       "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept"
//     }
//   }
// ];
