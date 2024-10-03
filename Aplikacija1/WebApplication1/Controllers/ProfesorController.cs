using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebApplication1.Models;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.Data;
using WebApplication1.DTOs;

namespace WebApplication1.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProfesorController : ControllerBase
    {
        private readonly FakultetContext _context;

        public ProfesorController(FakultetContext context)
        {
            _context = context;
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] ProfesorRegistrationDto profesorDto)
        {
            var profesor = new Profesor
            {
                Username = profesorDto.Username,
                Password = profesorDto.Password,
                Ime = profesorDto.Ime,
                Prezime = profesorDto.Prezime,
                Oblast = profesorDto.Oblast
            };

            _context.Profesori.Add(profesor);
            await _context.SaveChangesAsync();

            return Ok(profesorDto); 
        }



        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] ProfesorLoginDto profesorDto)
        {
            var profesor = await _context.Profesori
                .Include(p => p.ProfesorPredmeti)
                .ThenInclude(pp => pp.Predmet)
                .FirstOrDefaultAsync(p => p.Username == profesorDto.Username && p.Password == profesorDto.Password);

            if (profesor == null)
                return Unauthorized();

            var profesorResponse = new ProfesorDto
            {
                Id = profesor.Id,
                Username = profesor.Username,
                Ime = profesor.Ime,
                Prezime = profesor.Prezime,
                Oblast = profesor.Oblast,
                Predmeti = profesor.ProfesorPredmeti.Select(pp => new PredmetDto
                {
                    Id = pp.Predmet.Id,
                    Naziv = pp.Predmet.Naziv,
                    Semestar = pp.Predmet.Semestar
                }).ToList()
            };

            return Ok(profesorResponse);
        }



        [HttpGet("{id}/predmeti")]
        public async Task<IActionResult> GetPredmeti(int id)
        {
            var profesor = await _context.Profesori
                .Include(p => p.ProfesorPredmeti)
                .ThenInclude(pp => pp.Predmet)
                .FirstOrDefaultAsync(p => p.Id == id);

            if (profesor == null)
                return NotFound();

            var predmeti = profesor.ProfesorPredmeti
                .Select(pp => new PredmetDto
                {
                    Id = pp.Predmet.Id,
                    Naziv = pp.Predmet.Naziv,
                    Semestar = pp.Predmet.Semestar
                })
                .ToList();

            return Ok(predmeti);
        }

    }
}
