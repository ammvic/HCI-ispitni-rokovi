using Microsoft.AspNetCore.Mvc;
using WebApplication2.Models;
using Microsoft.EntityFrameworkCore;


namespace WebApplication2.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DestinacijeController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public DestinacijeController(ApplicationDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Destinacija>>> GetDestinacije()
        {
            return await _context.Destinacije.ToListAsync();
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Destinacija>> GetDestinacija(int id)
        {
            var destinacija = await _context.Destinacije.FindAsync(id);
            if (destinacija == null)
            {
                return NotFound();
            }
            return destinacija;
        }

        [HttpGet("search")]
        public async Task<ActionResult<IEnumerable<Destinacija>>> SearchDestinacije(string query)
        {
            return await _context.Destinacije
                .Where(d => d.Naziv.Contains(query))
                .ToListAsync();
        }
    }

}
